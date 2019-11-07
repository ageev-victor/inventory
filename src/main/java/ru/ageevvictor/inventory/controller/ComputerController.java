package ru.ageevvictor.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.ageevvictor.inventory.model.hardware.Computer;
import ru.ageevvictor.inventory.model.hardware.HardDrive;
import ru.ageevvictor.inventory.model.hardware.Monitor;
import ru.ageevvictor.inventory.model.service.CheckSubnet;
import ru.ageevvictor.inventory.model.service.ComputerDataParser;
import ru.ageevvictor.inventory.service.ComputerService;
import ru.ageevvictor.inventory.service.MonitorService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class ComputerController {

    private ComputerService computerService;
    private MonitorService monitorService;
    private ApplicationContext applicationContext;
    private ComputerDataParser computerDataParser;
    private ModelAndView modelAndViewComputers;
    private ModelAndView modelAndViewActions;

    {
        modelAndViewComputers = new ModelAndView("computers");
        modelAndViewActions = new ModelAndView("computer_actions");
    }

    @Autowired
    public void setComputerDataParser(ComputerDataParser computerDataParser) {
        this.computerDataParser = computerDataParser;
    }

    @Autowired
    public void setComputerService(ComputerService computerService) {
        this.computerService = computerService;
    }

    @Autowired
    public void setMonitorService(MonitorService monitorService) {
        this.monitorService = monitorService;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @RequestMapping("/computers/{id}")
    public ModelAndView viewComputersPage(@PathVariable(name = "id") long id) {
        List<Computer> listComputers = computerService.findBySubnetID(id);
        Collections.sort(listComputers);
        modelAndViewComputers.addObject("listComputers", listComputers);
        modelAndViewComputers.addObject("subnetID", listComputers.get(0).getSubnet().getId());
        return modelAndViewComputers;
    }

    @RequestMapping("/findpc")
    public ModelAndView viewComputerPage(@ModelAttribute(name = "pcname") String pcname) {
        List<Computer> listComputers = new ArrayList<>();
        Computer computer = computerService.getComputerByName(pcname);
        listComputers.add(computer);
        modelAndViewComputers.addObject("listComputers", listComputers);
        modelAndViewComputers.addObject("subnetID", listComputers.get(0).getSubnet().getId());
        return modelAndViewComputers;
    }

    @RequestMapping("/refreshStatus/{subnetID}")
    public String refreshStatus(@PathVariable(name = "subnetID") Long subnetID) {
        List<Computer> listComputers = computerService.findBySubnetID(subnetID);
        CheckSubnet checkSubnet = applicationContext.getBean(CheckSubnet.class);
        checkSubnet.refreshStatus(listComputers, computerService);
        return "redirect:/computers/{subnetID}";
    }

    @RequestMapping("/computer_actions/{id}")
    public ModelAndView pageActions(@PathVariable(name = "id") Long id) {
        Computer computer = computerService.get(id);
        modelAndViewActions.addObject("computer", computer);
        modelAndViewActions.addObject("subnetID", computer.getSubnet().getId());
        return modelAndViewActions;
    }

    @RequestMapping("/whoLoggedOn/{id}")
    public ModelAndView whoLoggedOn(@PathVariable(name = "id") Long id) {
        Computer computer = computerService.get(id);
        String login = computerDataParser.getWhoLoggedOn(computer.getName());
        modelAndViewActions.addObject("computer", computer);
        modelAndViewActions.addObject("login", login);
        modelAndViewActions.addObject("subnetID", computer.getSubnet().getId());
        return modelAndViewActions;
    }

    @RequestMapping("/getListPrinters/{id}")
    public ModelAndView getListPrinters(@PathVariable(name = "id") Long id) {
        Computer computer = computerService.get(id);
        List<String> listPrinters = computerDataParser.getListPrinters(computer.getName());
        modelAndViewActions.addObject("computer", computer);
        modelAndViewActions.addObject("listPrinters", listPrinters);
        modelAndViewActions.addObject("subnetID", computer.getSubnet().getId());
        return modelAndViewActions;
    }

    @RequestMapping("/deletePrinter/{id}")
    public ModelAndView deletePrinter(@PathVariable(name = "id") Long id, @ModelAttribute(name = "printer") String printer) {
        Computer computer = computerService.get(id);
        computerDataParser.deletePrinter(computer, printer);
        List<String> listPrinters = computerDataParser.getListPrinters(computer.getName());
        modelAndViewActions.addObject("computer", computer);
        modelAndViewActions.addObject("listPrinters", listPrinters);
        modelAndViewActions.addObject("subnetID", computer.getSubnet().getId());
        return modelAndViewActions;
    }

    @RequestMapping("/addprinter/{id}")
    public ModelAndView addprinter(@PathVariable(name = "id") Long id, @ModelAttribute(name = "printer") String printer) {
        Computer computer = computerService.get(id);
        computerDataParser.addPrinter(computer, printer, computer.getSubnet().getServerName());
        List<String> listPrinters = computerDataParser.getListPrinters(computer.getName());
        modelAndViewActions.addObject("computer", computer);
        modelAndViewActions.addObject("listPrinters", listPrinters);
        modelAndViewActions.addObject("subnetID", computer.getSubnet().getId());
        return modelAndViewActions;
    }

    @RequestMapping("/clearCacheJava/{id}")
    public ModelAndView clearCacheJava(@PathVariable(name = "id") Long id) {
        Computer computer = computerService.get(id);
        computer.getComputerDataParser().clearCacheJava(computer);
        modelAndViewActions.addObject("computer", computer);
        modelAndViewActions.addObject("subnetID", computer.getSubnet().getId());
        return modelAndViewActions;
    }

    @RequestMapping("/computerPowerOn/{id}")
    public ModelAndView computerPowerOn(@PathVariable(name = "id") Long id) {
        Computer computer = computerService.get(id);
        String server = computer.getSubnet().getServerName();
        computerDataParser.powerOnPc(server, computer.getMacAddress());
        modelAndViewActions.addObject("computer", computer);
        modelAndViewActions.addObject("subnetID", computer.getSubnet().getId());
        return modelAndViewActions;
    }

    @RequestMapping("/monitors/{id}")
    public ModelAndView viewMonitorsPage(@PathVariable(name = "id") long id) {
        List<Monitor> listMonitors = monitorService.getListMonitorsByComputer(id);
        ModelAndView mav = new ModelAndView("monitors");
        mav.addObject("listMonitors", listMonitors);
        return mav;
    }

    @RequestMapping("/hardDrives/{id}")
    public ModelAndView viewHardDrives(@PathVariable(name = "id") long id) {
        List<HardDrive> hardDrives = computerService.getListHardDrivesByComputer(id);
        ModelAndView mav = new ModelAndView("hard_drives");
        mav.addObject("hardDrives", hardDrives);
        return mav;
    }

    @RequestMapping("/computerPowerOff/{id}")
    public ModelAndView computerPowerOff(@PathVariable(name = "id") Long id) {
        Computer computer = computerService.get(id);
        computerDataParser.powerOffPc(computer);
        modelAndViewActions.addObject("computer", computer);
        modelAndViewActions.addObject("subnetID", computer.getSubnet().getId());
        return modelAndViewActions;
    }
}
