#!/usr/bin/perl

use strict;
use warnings;
use File::Temp qw(tempdir);

my $srcdir = "src/org/jnasockopt";
my $inputoption = "$srcdir/JNASockOption.java";
my $inputlevel = "$srcdir/JNASockOptionLevel.java";

my $tmpdir = tempdir(CLEANUP => 1);

my $tmpc = $tmpdir."/test.c";
my $tmpx = $tmpdir."/test";

sub checkSymbol
{
    my @includes = qw(stdio.h stdlib.h net/if_utun.h netinet/in.h sys/mbuf.h sys/un.h netinet/sctp.h netinet/tcp.h netinet/udp.h netpacket/packet.h);
    my $sym = shift @_;
    my $level = shift @_;
    my $func = $level?"putLevel":"putOption";
    my $enum = $level?"JNASockOptionLevel":"JNASockOption";
    open (OUTPUT, ">$tmpc") || die ("Cannot open C file $tmpc: $!");
    for my $i (@includes)
    {
	print OUTPUT "#include <$i>\n" if ( -r "/usr/include/$i" );
    }
    print OUTPUT <<"END1";

/* testing $sym */

int
main (int argc, char **argv)
{
    printf ("\\t\\t$func ($enum.$sym, 0x%x);\\n", $sym);
    exit (0);
}
END1
    close (OUTPUT);
    unlink ($tmpx);
    # printf STDERR "Testing $sym\n";
    my $out = "";
    my $redir = ">/dev/null 2>&1";
    open (PIPE, "gcc $tmpc -o $tmpx $redir && chmod 755 $tmpx && $tmpx|") || die "Cannot run gcc: $!";
    while (<PIPE>)
    {
	$out .= $_;
    }
    return $out;
}

sub checkFiles
{
    print << "END2";
package sockettest;

public class JNASockOptionDetailsSubclass extends JNASockOptionDetails {

\tpublic JNASockOptionDetailsSubclass() {
\t\tsuper ();
END2
    my $level;
    for ($level = 0; $level<2; $level++)
    {
	open (INPUT, $level?$inputlevel:$inputoption) || die ("Cannot open input file: $!");
	
	while (<INPUT>)
	{
	    print checkSymbol($1, $level) if (/^\s+([A-Z_]+)\b/);
	}
	
	close (INPUT);
    }
    print << "END3";
\t}
}
END3
}

checkFiles;
