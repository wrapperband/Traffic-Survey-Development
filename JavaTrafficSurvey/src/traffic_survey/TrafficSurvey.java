/**
 * 
 */
package traffic_survey;

/**
 * @author dels
 *
 */

import java.io.IOException;
import java.util.LinkedList;

import log.*;
import vehicles.*;

public class TrafficSurvey {

	/**
	 * @param args
	 */
	
	private static String logFileName;
	private static VehicleFactory vf;
	private static TrafficLog log;
	
	public static void main(String[] args) throws IOException {
		// parse command line args
		logFileName = "./test-data.txt";
		
		// Process a log file
		log = new TrafficLog(logFileName);
		
		try {
			// import the traffic log
			log.process();
			
			// get the (singleton) VehicleFactory
			vf = VehicleFactory.getInstance();
			
			// access the results
			LinkedList<Vehicle> up = vf.getList("Up");
			LinkedList<Vehicle> down = vf.getList("Down");
			
			System.out.println("Up - First 20 ------");
			for(int i=0; i<20; i++){
				Vehicle v = up.get(i);
				System.out.println( v.toString() );
			}
			System.out.println("--- Last 20 ---");
			for(int i=0; i<20; i++){
				Vehicle v = up.get(up.size()-20+i);
				System.out.println( v.toString() );
			}
			
			System.out.println("Down - First 20 ------");
			for(int i=0; i<20; i++){
				Vehicle v = down.get(i);
				System.out.println( v.toString() );
			}
			System.out.println("--- Last 20 ---");
			for(int i=0; i<20; i++){
				Vehicle v = down.get(down.size()-20+i);
				System.out.println( v.toString() );
			}
			
			System.out.println("Success: # Up="+ up.size() + ":\t # Down="+ down.size());
		}
		catch ( Exception ex) {
		}
		finally {
		}	
	}

}
