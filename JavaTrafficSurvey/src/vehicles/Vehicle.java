package vehicles;

import util.TimeDiff;

public class Vehicle {
	
	private long startD;
	private long startT;
	private long endT;
	private long duration;
	private long interval;
	private boolean isUp;
	
	// return a duplicate of a Vehicle
	public Vehicle copy() { return new Vehicle( startD, startT, endT, isUp, duration, interval ); }

	// accessor methods
	public void setStart( long newDay, long newStart) { startD = newDay; startT = newStart; }	
	public void setEnd( long newEnd, long previous) { endT = newEnd; duration = TimeDiff.timeDiff( endT, startT ); interval = TimeDiff.timeDiff(startT, previous); }
	public long getEnd() { return endT; }
	
	// construct Vehicle
	public Vehicle( long day, long start, long end, boolean dir ) {
		this(day,start,end,dir,0,0);
		duration = TimeDiff.timeDiff( end, start );
	}
	
	public Vehicle( long day, long start, long end, boolean dir, long dwell, long gap ) {
		startD = day;
		startT = start;
		endT = end;
		duration = dwell;
		interval = gap;
		isUp = dir;
	}

	public String toString() {
		return ((isUp?"UP":"DN")+","+startD+","+startT+","+endT+","+duration+","+interval);
	}
}
