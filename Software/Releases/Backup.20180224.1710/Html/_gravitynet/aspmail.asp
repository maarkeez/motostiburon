<%
SET Mailer=Server.CreateObject("SMTPsvg.Mailer")
path=lCase(trim(request.serverVariables("SERVER_NAME")&""))
referer=lCase(trim(request.serverVariables("HTTP_REFERER")&""))

IF referer<>"" THEN
 referer=right(referer,len(referer)-instr(referer,"//")-1)
 referer=left(referer,instr(referer,"/")-1)
END IF

IF referer<>path THEN
 %><font color=red>Acceso no autorizado</font><%
 response.end
END IF

cuerpo=""

FOR i=1 TO request.form.count
 nombre=request.form.key(i)
 valor=request.form.item(i)
 SELECT CASE nombre
  case "smtp"           smtp          =valor
  case "remitente"      remitente     =valor
  case "asunto"         asunto        =valor
  case "para"           para          =valor
  case "envio_correcto" envio_correcto=valor
  case "envio_erroneo"  envio_erroneo =valor
  case else             cuerpo        =cuerpo&nombre&" = "&valor&VBCRLF
 END SELECT
NEXT

SUB compruebaVar(Var)
 IF request.form(var)&""="" THEN
  %><font color=red>Error al enviar el formulario. Falta el campo obligatorio en el formulario de envio. Campo: "<%=var%>"</font><%
  response.end
 END IF
END SUB

compruebaVar "smtp"
compruebaVar "remitente"
compruebaVar "asunto"
compruebaVar "para"
compruebaVar "envio_correcto"
compruebaVar "envio_erroneo"

Mailer.RemoteHost=smtp
Mailer.FromName=remitente
Mailer.FromAddress=remitente
Mailer.Subject=asunto
Mailer.AddRecipient para,para
Mailer.BodyText=cuerpo
Mailer.charset=2

IF Mailer.sendmail THEN
 IF request("email")<>"" and request("mensaje")<>"" THEN
  Mailer.ClearBodyText
  Mailer.ClearRecipients
  Mailer.AddRecipient request("email"),request("email")
  Mailer.BodyText=request("mensaje")
  Mailer.sendmail
 END IF
 response.redirect(envio_correcto)
ELSE
 response.redirect(envio_erroneo)
END IF %>