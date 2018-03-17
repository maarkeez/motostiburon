angular.module("motosTiburon")
.constant("recogidaUrl", "/cgi-bin/presupuesto_controller.pl")
.constant("contactoUrl", "/cgi-bin/contacto_controller.pl")
.controller("motosTiburonCtrl",function($scope, $http, $location,recogidaUrl,contactoUrl){
	$scope.recogida={};
	$scope.recogida.origen="";
	$scope.recogida.fechaRecogida="";
	$scope.recogida.destino="";
	$scope.recogida.fechaEntrega="";
	$scope.recogida.email="";


	$scope.updateRecogida = function () {
		$scope.recogida.origen=document.getElementById('origen').value;
		$scope.recogida.destino=document.getElementById('destino').value;
	}

	$scope.sendSolicitudPresupuesto = function (detallesRecogida) {
		var recogida = $.param(angular.copy(detallesRecogida));
		console.log(recogida);
		var postHeaders = {headers:{'Content-Type': 'application/x-www-form-urlencoded'}};
		
		$http.post(recogidaUrl, recogida,postHeaders)
		.then(function(success) {
			console.log(success);
			$scope.message = success.data;
			$scope.message.show = true;

			if(success.data.status == 200){
				$scope.message.class =  "alert-success";
			}else{
				$scope.message.class =  "alert-danger";
			} 
		});
	}


	
	$scope.sendContactoMensaje = function (mensaje) {
		var mensaje = $.param(angular.copy(mensaje));
		console.log(mensaje);
		var postHeaders = {headers:{'Content-Type': 'application/x-www-form-urlencoded'}};
		
		$http.post(contactoUrl, mensaje,postHeaders)
		.then(function(success) {
			console.log(success);
			$scope.message = success.data;
			$scope.message.show = true;

			if(success.data.status == 200){
				$scope.message.class =  "alert-success";
			}else{
				$scope.message.class =  "alert-danger";
			} 
		});
	}

})