# Form Value EMailer
#   by Raymond P. Burkholder  <rpb@ba.ca> 
#   Burkholder & Associates, Inc., 
#   Phone 519 570 4251, 800-694-0922
#
# This version dated:  1996/12/27
# 
# Tested with Perl 5.003 on Windows NT 4.0 Server, MS IIS/2.0
# 
# With thanx to Christian Mallwitz for the SendMail portion.
# With thanx to Matt Wright for code for processing Form Post variables
#
# Required variables to be posted (all names must be in lower case):
#  _to 		email address to whom the values should be sent, eg who@are.you
#  _smtp	address of email server, eg are.you
#  _subject	content to be placed into the subject header of the message
#  _nextpage	if supplied, this page will be returned to the browser.
#

$allow_html = 1;        # 1 = Yes; 0 = No

MAIN: {

  # Get the input
  read(STDIN, $buffer, $ENV{ 'CONTENT_LENGTH' } );

  # Split the name-value pairs
  @pairs = split(/&/, $buffer);

  $ix = 0;
  foreach $pair (@pairs) {
     ($name, $value) = split(/=/, $pair);
  
     # Un-Webify plus signs and %-encoding
     $value =~ tr/+/ /;
     $value =~ s/%([a-fA-F0-9][a-fA-F0-9])/pack("C", hex($1))/eg;
     $value =~ s/<!--(.|\n)*-->//g;
  
     if ( 1 != $allow_html ) {
       $value =~ s/<([^>]|\n)*>//g;
       }
  
    $FORM{ $name } = $value;

    # send only the variables not prefixed with '_'  
    if ( $name !~ m/^_/ ) {
      $listPairs[ $ix++ ] = join "\t", $name, $value;
      }
    }

  $listLines = join "\n", @listPairs;

  # print
  # sendmail($from, $reply, $to, $smtp, $subject, $message );
    $result = sendmail( $FORM{ '_to'} , 
                        $ENV{ 'REMOTE_ADDR'}, 
                        $FORM{ '_to' }, $FORM{ '_smtp' }, $FORM{ '_subject' },
                        $listLines );

  if ( defined( $FORM{ '_nextpage' } ) ) {
    SendNewURL( $result );
    }
  else {
    SendDefaultForm( $result );
    }
  exit 0;
  }

sub SendDefaultForm {

  my ( $resultcode ) = @_;

  print $ENV{ 'SERVER_PROTOCOL' }, " 200 OK\n";
  print "Content-type: text/html\n\n";
  print "<html><head><title>Formulario enviado</title>";
  print "</head><body>\n";

  print '<!-- sendmail had return code = ', $resultcode, ' -->', "\n";
  print "<br><br><b>Gracias, su peticion ha sido enviada.</b>\n";
  

  print "</body></html>\n";
  }

sub SendNewURL {

  my ( $resultcode ) = @_;

  print $ENV{ 'SERVER_PROTOCOL' }, " 303 See Other\n";
  print "Location: ", $FORM{ '_nextpage' }, "\n" ;
  print "Content-type: text/html\n\n";
  print "<html><head><title>Redirection Link</title>";
  print "</head><body>\n";

  print '<!-- sendmail had return code = ', $resultcode, ' -->', "\n";
  print "<br><br><b>Thank you, your request has been sent.</b>\n";
  print "<br>Please go to this <a href=\"", $FORM{ '_nextpage' }, "\">page</a>.\n";

  print "</body></html>\n";
  }

#------------------------------------------------------------
# sub sendmail()
#
# send/fake email around the world ...
#
# Version : 1.21
# Environment: Hip Perl Build 105 NT 3.51 Server SP4
# Environment: Hip Perl Build 110 NT 4.00
#
# arguments:
#
# $from email address of sender
# $reply email address for replying mails
# $to email address of reciever
# (multiple recievers can be given separated with space)
# $smtp name of smtp server (name or IP)
# $subject subject line
# $message (multiline) message
#
# return codes:
#
# 1 success
# -1 $smtphost unknown
# -2 socket() failed
# -3 connect() failed
# -4 service not available
# -5 unspecified communication error
# -6 local user $to unknown on host $smtp
# -7 transmission of message failed
# -8 argument $to empty
#
# usage examples:
#
# print
# sendmail("Alice <alice\@company.com>",
# "alice\@company.com",
# "joe\@agency.com charlie\@agency.com",
# $smtp, $subject, $message );
#
# or
#
# print
# sendmail($from, $reply, $to, $smtp, $subject, $message );
#
# (sub changes $_)
#
#------------------------------------------------------------

use Socket;

sub sendmail {

my ($from, $reply, $to, $smtp, $subject, $message) = @_;

my ($fromaddr) = $from;
my ($replyaddr) = $reply;

$to =~ s/[ \t]+/, /g; # pack spaces and add comma
$fromaddr =~ s/.*<([^\s]*?)>/$1/; # get from email address
$replyaddr =~ s/.*<([^\s]*?)>/$1/; # get reply email address
$replyaddr =~ s/^([^\s]+).*/$1/; # use first address
$message =~ s/^\./\.\./gm; # handle . as first character
$message =~ s/\r\n/\n/g; # handle line ending
$message =~ s/\n/\r\n/g;
$smtp =~ s/^\s+//g; # remove spaces around $smtp
$smtp =~ s/\s+$//g;

if (!$to) { return -8; }

my($proto) = (getprotobyname('tcp'))[2];
my($port) = (getservbyname('smtp', 'tcp'))[2];

my($smtpaddr) = ($smtp =~
/^(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})$/)
? pack('C4',$1,$2,$3,$4)
: (gethostbyname($smtp))[4];

if (!defined($smtpaddr)) { return -1; }

if (!socket(S, AF_INET, SOCK_STREAM, $proto)) { 
  return -2; }
if (!connect(S, pack('Sna4x8', AF_INET, $port, $smtpaddr))) { 
  return -3; }

my($oldfh) = select(S); $| = 1; select($oldfh);

$_ = <S>; if (/^[45]/) { close S; return -4; }

print S "helo localhost\r\n";
$_ = <S>; if (/^[45]/) { close S; return -5; }

print S "mail from: <$fromaddr>\r\n";
$_ = <S>; if (/^[45]/) { close S; return -5; }

foreach (split(/, /, $to)) {
  print S "rcpt to: <$_>\r\n";
  $_ = <S>; if (/^[45]/) { close S; return -6; }
  }

print S "data\r\n";
$_ = <S>; if (/^[45]/) { close S; return -5; }

print S "To: $to\r\n";
print S "From: $from\r\n";
print S "Reply-to: $replyaddr\r\n" if $replyaddr;
print S "X-Mailer: Perl Sendmail Version 1.21 Christian Mallwitz Germany\r\n";
print S "Subject: $subject\r\n\r\n";
print S "$message";
print S "\r\n.\r\n";

$_ = <S>; if (/^[45]/) { close S; return -7; }

print S "quit\r\n";
$_ = <S>;

close S;
return 1;
}