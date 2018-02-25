#!C:\Strawberry\perl\bin\perl.exe

#============================================================================================================================
#
#	Nombre:		libreria_emails.pm
#	Descripción:	Librería que implementa los métodos para:		
#							Enviar emails a través del servidor de gmail
#										
#	Versión:	25-02-2018
#	Implementado para: Motos Tiburón
#	Autor:	David Márquez Delgado
#
#===========================================================================================================================
use strict;
use warnings;
use Email::Send::SMTP::Gmail;

# ================
# Ejemplos de uso
# ================
# &EnviarCorreo($destinatarios,$asunto,$mensaje);
# ================
# FIN ejemplos
# ================
sub enviarCorreo(){

	# Recibir los parámetros, tanto adjuntos como destinatarios son referencias a arrays de strings
	my ($destinatarios,$asunto,$mensaje) =@_;
	
	# Conectar con el servidor smtp de gmail
	my ($mail,$error)=Email::Send::SMTP::Gmail->new( -smtp=>'smtp.gmail.com',
                                                    -login=>'noreply.motostiburon@gmail.com',
                                                    -pass=>'1234567890qwert');

	# Comprobar conexión correcta
	if($mail==-1){
		printJsonResponse(500,"Error al intentar conectar con el servidor de correo: $error");
		return -1;
	}

	# Enviar email
	$mail->send(
		-to=>"$destinatarios",
		-subject=>"$asunto",
		-body=>"$mensaje");

	# Finalizar envío
	$mail->bye;
	return 0;
}

# Definición:	Sustituye las comillas de una cadena de carácteres por un espacio en blanco, Con esto permite conservar la estructura de la llamada al systema cuando se pretende mandar un email con mutt.
sub sustituirComillas(){
	# Recibir paráemtros
	my ($cadena) = @_;
	# Sustituir comillas dobles por espacio en blanco cuantas veces aparezca en la cadena de carácteres
	$cadena=~ s/"/ /g;
	# Devolver la cadena de carácteres transformada.
	return $cadena;
}


sub sustituirArroba(){
	# Recibir paráemtros
	my ($cadena) = @_;
	# Sustituir comillas dobles por espacio en blanco cuantas veces aparezca en la cadena de carácteres
	$cadena=~ s/\@/&#64;/g;
	# Devolver la cadena de carácteres transformada.
	return $cadena;
}



sub printJsonResponse(){
	my ($status,$message) =@_;

print <<EOF;
{"status":$status,"message":"$message"}
EOF

}


1;	; # Necesario para el correcto funcionamiento de las librerías en perl.
