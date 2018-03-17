#!perl

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

use lib 'D:\\XVRT\\motostiburon.es\\Html\\cgi-bin';	# Directorio de la librería de emails;

use libreria;
use CGI;	# Librería para controlar las variables y objetos del CGI



#############################################################################################################################
#	Variables 
#############################################################################################################################

# ---------------------------
# Declaración de variables:
# ---------------------------
my $path = 'D:\\XVRT\\motostiburon.es\\Html\\cgi-bin';

# Obtenemos el cgi con las variables:
my $cgi = CGI->new;

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

	my ($path,$origen,$fecha_origen,$destino,$fecha_destino,$destinatarios) =@_;

	my $asunto=qq(Consulta de presupuesto);
	my $mensaje=qq(
Este mensaje ha sido enviado autom&aacute;ticamente desde wwww.motostiburon.es<br/>
Recogida de moto en $origen el d&iacute;a $fecha_origen.<br/>
Llevar la moto a $destino el d&iacute;a $fecha_destino.<br/>
	);
	
	#my $valor = &enviarCorreo($destinatarios,$asunto,$mensaje);
	my $valor = &sendEmail($path,$destinatarios,$asunto,$mensaje);
	return $valor;
}


# ---------------------------------------------------------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------------------------------------------------------

# Imprimir la cabecera del documento
print "Cache-Control: no-cache,must-revalidate\n";
print "Pragma: no-cache\n";
#print "Content-type: application/json\n\n";
print "Content-type: text/html\n\n";

# Recuperar variables del formulario recibido
my $origen = $cgi->param('origen') || "";
my $fechaRecogida = $cgi->param('fechaRecogida') || "";
my $destino = $cgi->param('destino') || "";
my $fechaEntrega = $cgi->param('fechaEntrega') || "";
my $email = $cgi->param('email') || "";

# Enviar email 
#my $enviado = 0;
my $enviado = &emailRecogida($path,$origen,$fechaRecogida,$destino,$fechaEntrega,$email);

# Indicar que el email ha sido enviado
if($enviado == 0){
	&printJsonResponse(200,"Su presupuesto ha sido solicitado, pronto recibirá un email. Muchas gracias!");
} else {
	&printJsonResponse(500,"Lo sentimos, se ha producido un error al intentar conectar con el servidor de correo.");
}

1;