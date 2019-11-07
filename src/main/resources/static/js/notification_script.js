function refresh() {
	setInterval('timeout()', 5000);
}

function checkSubnetMessage() {
	alert("Проверка отделения...")
}

function startInventoryMessage() {
	alert("Инвентаризация всех отделений...")
}

function stopInventoryMessage() {
	alert("Инвентаризация остановлена...")
}


function timeout() {
	$('#progress_table').load(document.URL + ' #progress_table');
}

function checkUptime() {
	alert("Запуск проверки по таймеру!")
}

function stopCheckUptime() {
	alert("Проверка по таймеру остановлена!")
}

