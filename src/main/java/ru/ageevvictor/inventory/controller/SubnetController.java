package ru.ageevvictor.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.ageevvictor.inventory.model.*;
import ru.ageevvictor.inventory.model.hardware.Subnet;
import ru.ageevvictor.inventory.model.service.CheckServersTask;
import ru.ageevvictor.inventory.service.ComputerService;
import ru.ageevvictor.inventory.service.SubnetService;

import java.util.*;

@Controller
public class SubnetController {

    private boolean checkServersUptimeIsActive = false;
    private boolean inventoryIsActive = false;
    private SubnetService subnetService;
    private ComputerService computerService;
    private StartDataInitializer startDataInitializer;
    private ApplicationContext applicationContext;
    private Timer uptimeTimer;

    @Autowired
    public void setSubnetService(SubnetService subnetService) {
        this.subnetService = subnetService;
    }

    @Autowired
    public void setComputerService(ComputerService computerService) {
        this.computerService = computerService;
    }

    @Autowired
    public void setStartDataInitializer(StartDataInitializer startDataInitializer) {
        this.startDataInitializer = startDataInitializer;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @RequestMapping("/checkStatusSubnets")
    public String checkStatustSubnets() {
        CheckServersTask checkServersTask = applicationContext.getBean(CheckServersTask.class);
        uptimeTimer = new Timer();
        uptimeTimer.scheduleAtFixedRate(checkServersTask, 1000, 300000);
        checkServersUptimeIsActive = true;
        return "redirect:/subnets";
    }


    @RequestMapping("/stopCheckStatusSubnets")
    public String stopCheckStatustSubnets() {
        uptimeTimer.cancel();
        checkServersUptimeIsActive = false;
        return "redirect:/subnets";
    }


    @RequestMapping("/startTotalInventory")
    public String startService() {
        inventoryIsActive = true;
        new Thread(() -> {
            StartDataInitializer startDataInitializer = applicationContext.getBean(StartDataInitializer.class);
            startDataInitializer.inventoryAllSubnets();
        }).start();
        return "redirect:/subnets";
    }

    @RequestMapping("/stopTotalInventory")
    public String stopService() {
        inventoryIsActive = false;
        startDataInitializer.getSubnetList().forEach(subnet -> {
            if (subnet.getExecutorService() != null) subnet.getExecutorService().shutdownNow();
        });
        startDataInitializer.getSubnetList().clear();
        return "redirect:/subnets";
    }


    @RequestMapping("/subnets")
    public ModelAndView viewMainPage() {
        List<Subnet> listSubnets = subnetService.listAll();
        int countComputers = computerService.listAll().size();
        List<Subnet> offlineSubnets = new ArrayList<>();
        Collections.sort(listSubnets);
        ModelAndView mav = new ModelAndView("subnets");
        listSubnets.forEach(subnet -> {
            if (!subnet.isOnline()) offlineSubnets.add(subnet);
        });
        mav.addObject("listSubnets", listSubnets);
        mav.addObject("countComputers", countComputers);
        mav.addObject("checkServersUptimeIsActive", checkServersUptimeIsActive);
        mav.addObject("inventoryIsActive", inventoryIsActive);
        mav.addObject("offlineSubnets", offlineSubnets);
        return mav;
    }


    @RequestMapping("/newSubnet")
    public String showNewBranchPage(Model model) {
        Subnet subnet = applicationContext.getBean(Subnet.class);
        model.addAttribute("subnet", subnet);
        return "new_subnet";
    }

    @RequestMapping(value = "/saveSubnet", method = RequestMethod.POST)
    public String saveSubnet(@ModelAttribute("subnet") Subnet subnet) {
        subnet.setLastScanDate(new Date(0));
        subnet.setCountComputers(0);
        subnetService.save(subnet);
        return "redirect:/subnets";
    }


    @RequestMapping("/editSubnet/{id}")
    public ModelAndView showEditSubnetPage(@PathVariable(name = "id") Long id) {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        String role = authUser.getAuthorities().toString();
        ModelAndView adminMav = new ModelAndView("edit_subnet");
        ModelAndView userMav = new ModelAndView("no_permissions");
        ModelAndView resultMav;
        if (role.equals("[ADMIN]")) {
            Subnet subnet = subnetService.get(id);
            adminMav.addObject("subnet", subnet);
            adminMav.addObject("role", role);
            resultMav = adminMav;
        } else {
            userMav.addObject("role", role);
            resultMav = userMav;
        }
        return resultMav;
    }

    @RequestMapping("/checkCurrentSubnet/{id}")
    public String checkCurrentSubnet(@PathVariable(name = "id") Long id) {
        new Thread(() -> {
            StartDataInitializer startDataInitializer = applicationContext.getBean(StartDataInitializer.class);
            startDataInitializer.inventoryOneSubnet(id);
        }).start();

        return "redirect:/subnets/";
    }

    @RequestMapping("/deleteSubnet/{id}")
    public String deleteSubnet(@PathVariable(name = "id") Long id) {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        String role = authUser.getAuthorities().toString();
        String result = "redirect:/subnets";
        if (role.equals("[ADMIN]")) {
            subnetService.delete(id);
        } else result = "no_permissions";
        return result;
    }


}
