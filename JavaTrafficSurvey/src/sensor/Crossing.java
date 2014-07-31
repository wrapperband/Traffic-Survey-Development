package sensor;

public class Crossing {

	protected int sensor;
	protected long timeStamp;
	
	public long getTime() { return timeStamp; }
	public int getSensor() { return sensor; }
	
	public Crossing( String aSensor, long aTimeStamp) {
		sensor = aSensor.equals("A") ? 0 : 1;
		timeStamp = aTimeStamp;
		// System.out.println("new Crossing("+sensor+", "+timeStamp+")");
	}
}
