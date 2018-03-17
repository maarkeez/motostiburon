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
use File::Basename qw();
my ($name, $path, $suffix) = File::Basename::fileparse($0);

use lib 'D:\\XVRT\\motostiburon.es\\Html\\cgi-bin';	# Directorio de la librería de emails;

use libreria;
use CGI;	# Librería para controlar las variables y objetos del CGI



#############################################################################################################################
#	Variables 
#############################################################################################################################

# ---------------------------
# Declaración de variables:
# ---------------------------

# Obtenemos el cgi con las variables:
my $cgi = CGI->new;


#############################################################################################################################
# 	Funcionalidad
#############################################################################################################################

# ---------------------------------------------------------------------------------------------------------------------------
# Declaración de funciones
# ---------------------------------------------------------------------------------------------------------------------------

# Función para enviar el email de contacto
sub emailContacto(){

	my ($path,$mensaje,$destinatarios) =@_;

	my $asunto=qq(Informacion motos tiburon);
	
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
my $destinatarios = $cgi->param('destinatarios') || "";
my $mensaje = $cgi->param('mensaje') || "";

# Enviar email 
my $enviado = &emailContacto($path,$mensaje,$destinatarios);

# Indicar que el email ha sido enviado
if($enviado == 0){
	&printJsonResponse(200,"Su consulta se ha realizado con éxito, pronto recibirá un email. Muchas gracias!");
} else {
	&printJsonResponse(500,"Lo sentimos, se ha producido un error al intentar conectar con el servidor de correo.");
}

1;