package util;

public class TimeDiff {

	public static final int MS_PER_DAY = 24*60*60*1000;

	public static long timeDiff( long t2, long t1 ) {
		// subtract t1 from t2
		// add 24 hours if crossing midnight
		return (t2 + MS_PER_DAY - t1) % MS_PER_DAY;
	}


}
