/**
 * 
 */
package log;

import java.io.*;

import sensor.*;
import vehicles.*;

/**
 * @author dels
 *
 */


public class TrafficLog {

	private String logFile;
	private VehicleFactory vf;
	private static Crossing xNext;
	
	public boolean process( ) throws IOException {
		boolean status = false;
		String line;
		try {
			BufferedReader in = new BufferedReader(new FileReader(logFile));
			// for lines in logFile
			while ( (line = in.readLine()) != null ) {
				// construct a Crossing from line
				xNext = new Crossing( line.substring(0, 1), Long.parseLong(line.substring(1,line.length())) );
				// get the VehicleFactory to analyse it
				vf.analyse( xNext);
			}						
			// return true on success
			System.out.println("TrafficLog.process Complete");
			in.close();
			status = true;
		}
		finally {}

		return status;
	}
	
	// Constructor: 
	public TrafficLog( String fileName) {
		vf = VehicleFactory.getInstance();
		logFile = fileName;
		xNext = null;
		System.out.println("new TrafficLog");
	}
	
}
