$(document).ready(function() {
	// Inicializar valores para datepicker

	$('.datepicker').datepicker({
		weekStart : 1,
		startDate : "today",
		todayBtn : "linked",
		language : "es",
		daysOfWeekHighlighted : "0,6",
		autoclose : true
	});

	// Establece datepicker sobre el resto de elementos de la interfaz
	function beforeShowDatePicker() {
		$('.datepicker-dropdown').css('z-index', 99999);
	}

	$(function() {
		// Actualizar datepicker al ser seleccionado
		$(".datepicker").focus(function() {
			beforeShowDatePicker();
		});

		// Actualizar datepicker al modificar el tamaño del browser
		$(window).bind('resize', function(e) {
			window.resizeEvt;
			$(window).resize(function() {
				clearTimeout(window.resizeEvt);
				window.resizeEvt = setTimeout(function() {
					beforeShowDatePicker();
				}, 200);
			});
		});
	});
});