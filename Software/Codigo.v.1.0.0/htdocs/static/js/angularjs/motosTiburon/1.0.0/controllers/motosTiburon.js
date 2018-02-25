angular.module("motosTiburon")
.constant("recogidaUrl", "/cgi-bin/email_controller.cgi")
.controller("motosTiburonCtrl",function($scope, $http, $location,recogidaUrl){
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
})