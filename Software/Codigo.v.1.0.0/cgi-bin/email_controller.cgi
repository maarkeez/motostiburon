#!C:\Strawberry\perl\bin\perl.exe

#============================================================================================================================
#
#	Nombre:		email_controler.cgi
#	Descripción:	Controlador que implementa los métodos para:		
#							- Recibir mensaje POST y enviar email con la plantilla de solicitud de presupuesto al usuario.
#							- IMPORTANTE: Sólo procesa: application/x-www-form-urlencoded
#										
#	Versión:	25-02-2018
#	Implementado para: Motos Tiburón
#	Autor:	David Márquez Delgado
#
#===========================================================================================================================


#############################################################################################################################
#	Librerías
#############################################################################################################################
use strict;
use warnings;

use Cwd 'abs_path'; # Permite objener el path absoulto del script;
use lib abs_path() . '/lib';	# Directorio de la librería de emails;
use libreria_emails;
use CGI;	# Librería para controlar las variables y objetos del CGI

use Data::Dumper; # Debug: Print hash


#############################################################################################################################
#	Variables 
#############################################################################################################################

# ---------------------------
# Declaración de variables:
# ---------------------------

# Obtenemos el cgi con las variables:
my $cgi = CGI->new;

# Variables recibidas por POST y/o GET
my %input = $cgi->Vars; # Debe de ser Content-type: application/x-www-form-urlencoded



#############################################################################################################################
# 	Funcionalidad
#############################################################################################################################

# ---------------------------------------------------------------------------------------------------------------------------
# Declaración de funciones
# ---------------------------------------------------------------------------------------------------------------------------

# Función para enviar el email de recogida.
# Ejemplo de uso: 
# 	&emailRecogida($origen,$fecha_origen,$destino,$fecha_destino);
sub emailRecogida(){

	my ($origen,$fecha_origen,$destino,$fecha_destino,$destinatarios) =@_;

	my $asunto=qq(Consulta de presupuesto);
	my $mensaje=qq(
Este mensaje ha sido enviado automáticamente desde wwww.motostiburon.es
Recogida de moto en $origen el día $fecha_origen.
Llevar la moto a $destino el día $fecha_destino.
	);
	
	my $valor = &enviarCorreo($destinatarios,$asunto,$mensaje);
	return $valor;
}


# ---------------------------------------------------------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------------------------------------------------------

# Imprimir la cabecera del documento
print "Cache-Control: no-cache,must-revalidate\n";
print "Pragma: no-cache\n";
print "Content-type: application/json\n\n";

# Recuperar variables del formulario recibido
my $origen = $input{'origen'} || "";
my $fechaRecogida = $input{'fechaRecogida'} || "";
my $destino = $input{'destino'} || "";
my $fechaEntrega = $input{'fechaEntrega'} || "";
my $email = $input{'email'} || "";

# Enviar email 
my $enviado = &emailRecogida($origen,$fechaRecogida,$destino,$fechaEntrega,$email);

# Indicar que el email ha sido enviado
if($enviado == 0){
	&printJsonResponse(200,"Su presupuesto ha sido solicitado, pronto recibirá un email. Muchas gracias!");
}