# file:		parseDtsEvents.pl
# author:	Del Smith (UEMG) +61 438 824 469
# date:		2014-04-07
#
# parse the LOG file and extract individual events to a CSV record format
#
# get lines from standard input (until EOF)

open INPUT, "TRP201406111630.txt";

while (<INPUT>) {
    chomp();

# skip lines until we hit a "- Event -" signature
    if ( m/- Event -/ ) {
        # combine next two lines and extract (Table,Point,Type,Severity,Time,Description,Message)
	chomp( $l1=<> );
	$l1 =~ s/Table: //;
    	$l1 =~ s/ Point: //;
        $l1 =~ s/ Type: /,/;
        $l1 =~ s/ Severity: /,/;
        $l1 =~ s/ $/,/;
        # print STDOUT ( $l1 );

	chomp( $l2=<> );
        $t1 = $l2;
        $t1 =~ s/^.{1,}2014 //;
        $t1 =~ s/'.{1,}$//;
        #print STDOUT ( $t1, "\n");
        @time = split /([::.])/, $t1;
        printf STDOUT "%02d:%02d:%02d.%03d", $time[0],$time[2],$time[4]+$time[6];


        $l2 =~ s/^Time :'\d{2}\/\d{2}\/\d{4} //;
        $l2 =~ s/' Description:'/,/;
        $l2 =~ s/' Message: '/,/;
        $l2 =~ s/'$//;
        # print STDOUT ( $l2,"\n");
    }
}
