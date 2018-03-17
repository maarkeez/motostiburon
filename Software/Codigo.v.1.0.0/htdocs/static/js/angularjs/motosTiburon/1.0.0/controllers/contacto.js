angular.module("motosTiburon")
.controller("contactoCtrl",function($scope){
	startCustomDatePicker();
	startAutoScroll();

	$scope.contacto={};
	$scope.contacto.mensaje={};
	$scope.contacto.mensaje.destinatarios="";
	$scope.contacto.mensaje.mensaje="";

})