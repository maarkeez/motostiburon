#!C:\Strawberry\perl\bin\perl.exe

#############################################################################################################################
#	Librerías
#############################################################################################################################
use strict;
use warnings;

use lib './lib';	# Directorio de la librería de emails;
use libreria_emails;
use CGI;	# Librería para controlar las variables y objetos del CGI

#############################################################################################################################
#	Variables 
#############################################################################################################################

# ---------------------------
# Declaración de variables:
# ---------------------------

# Obtenemos el cgi con las variables:
my $cgi = CGI->new;

# Variables recibidas por POST y/o GET
my %input = $cgi->Vars;


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
	
	&EnviarCorreo($destinatarios,$asunto,$mensaje);
}

sub printJsonResponse(){
	my ($status,$message) =@_;

print <<EOF;
{"status":$status,"message":"$message"}
EOF

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
my $fecha_origen = $input{'fecha_origen'} || "";
my $destino = $input{'destino'} || "";
my $fecha_destino = $input{'fecha_destino'} || "";
my $destinatarios = $input{'destinatarios'} || "";

# Enviar email 
&emailRecogida($origen,$fecha_origen,$destino,$fecha_destino,$destinatarios);

# Indicar que el email ha sido enviado
&printJsonResponse(200,"Email enviado correctamente");