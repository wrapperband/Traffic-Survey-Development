/**
 * 
 */
package vehicles;

/**
 * @author dels
 *
 */

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import util.*;
import sensor.*;

public class VehicleFactory {
	
	// constants
	static int DT1 = 72; 
	static int DT2 = 72; 
	static int DT3 = 2000; 
	static int SENSOR_A = 0, SENSOR_B = 1;
	
	// state variables
	private static VF_st state;
	private static long prevTime;
	private static Vehicle carUp, carDn;
	private int day;
	private long timeStamp;
	private int sensor;
	private long deltaT;
	
	// content
	private static LinkedList<Vehicle> upList;
	private static LinkedList<Vehicle> downList;
	
	// accessor for the Vehicle Lists
	public LinkedList<Vehicle> getList( String which ) {
		if ( which.equals("Up") ) 
			return upList;
		else 
			return downList;
	}
	
	/*
	 *  finish off a vehicle (trip) and put it on a List
	 */
	private void saveVehicle( Vehicle which, long timeStamp, LinkedList<Vehicle> list ) {
		which.setEnd(timeStamp, which.getEnd());
		// which.print();
		Vehicle save = which.copy();
		list.addLast(save);
	}
	
	/*
	 * FSM Events
	 */
	private VF_ev getEvent() {
		if (sensor == SENSOR_A) {
			if      ( deltaT < DT1 ) return VF_ev.A1;
			else if ( deltaT < DT2 ) return VF_ev.A2;
			else if ( deltaT < DT3 ) return VF_ev.A3;
			else                     return VF_ev.A4;
		} else if   ( deltaT < DT1 ) return VF_ev.B1;		
		return VF_ev.NA;
	}
	
	/*
	 * 	FSM transition map
	 * 		keyed on a (state,event) combination
	 * 		transition contains (action,final_state)
	 */
	private static Map<String,VF_trans> FsmMap;
	
	private void setupFsmMap() {
		FsmMap = new HashMap<String,VF_trans>();
		//                 ( event ,  state )                  (    action , next state )
		FsmMap.put( VF_tag(VF_ev.A1,VF_st.IDLE), new VF_trans(VF_act.DN_NEW,VF_st.DOWN_1));
		FsmMap.put( VF_tag(VF_ev.A2,VF_st.IDLE), new VF_trans(VF_act.DN_NEW,VF_st.DOWN_1));
		FsmMap.put( VF_tag(VF_ev.A3,VF_st.IDLE), new VF_trans(VF_act.DN_NEW,VF_st.DOWN_1));
		FsmMap.put( VF_tag(VF_ev.A4,VF_st.IDLE), new VF_trans(VF_act.DN_NEW,VF_st.DOWN_1));		
		FsmMap.put( VF_tag(VF_ev.A1,VF_st.DOWN_1), new VF_trans(VF_act.UP_NEW,VF_st.CC_1));
		FsmMap.put( VF_tag(VF_ev.A2,VF_st.DOWN_1), new VF_trans(VF_act.UP_NEW,VF_st.OL1_2));
		FsmMap.put( VF_tag(VF_ev.A3,VF_st.DOWN_1), new VF_trans(VF_act.DN_SAVE,VF_st.IDLE));
		FsmMap.put( VF_tag(VF_ev.B1,VF_st.DOWN_1), new VF_trans(VF_act.UP_NEW,VF_st.UP_1));		
		FsmMap.put( VF_tag(VF_ev.A1,VF_st.DOWN_2), new VF_trans(VF_act.DN_SAVE,VF_st.IDLE));
		FsmMap.put( VF_tag(VF_ev.A2,VF_st.DOWN_2), new VF_trans(VF_act.DN_SAVE,VF_st.IDLE));
		FsmMap.put( VF_tag(VF_ev.A3,VF_st.DOWN_2), new VF_trans(VF_act.DN_SAVE,VF_st.IDLE));
		FsmMap.put( VF_tag(VF_ev.A4,VF_st.DOWN_2), new VF_trans(VF_act.DN_SAVE,VF_st.IDLE));		
		FsmMap.put( VF_tag(VF_ev.A1,VF_st.UP_1), new VF_trans(VF_act.NONE,VF_st.CC_2));
		FsmMap.put( VF_tag(VF_ev.A2,VF_st.UP_1), new VF_trans(VF_act.NONE,VF_st.OL1_1));
		FsmMap.put( VF_tag(VF_ev.A3,VF_st.UP_1), new VF_trans(VF_act.NONE,VF_st.UP_2));		
		FsmMap.put( VF_tag(VF_ev.B1,VF_st.UP_2), new VF_trans(VF_act.UP_SAVE,VF_st.IDLE));		
		FsmMap.put( VF_tag(VF_ev.A1,VF_st.OL1_1), new VF_trans(VF_act.NONE,VF_st.OL1_2));
		FsmMap.put( VF_tag(VF_ev.A2,VF_st.OL1_1), new VF_trans(VF_act.NONE,VF_st.OL1_2));
		FsmMap.put( VF_tag(VF_ev.A3,VF_st.OL1_1), new VF_trans(VF_act.NONE,VF_st.OL1_2));
		FsmMap.put( VF_tag(VF_ev.A4,VF_st.OL1_1), new VF_trans(VF_act.NONE,VF_st.OL1_2));		
		FsmMap.put( VF_tag(VF_ev.B1,VF_st.OL1_2), new VF_trans(VF_act.UP_SAVE,VF_st.DOWN_2));		
		FsmMap.put( VF_tag(VF_ev.B1,VF_st.OL2_1), new VF_trans(VF_act.NONE,VF_st.OL2_2));		
		FsmMap.put( VF_tag(VF_ev.A2,VF_st.OL2_2), new VF_trans(VF_act.DN_SAVE,VF_st.OL2_3A));
		FsmMap.put( VF_tag(VF_ev.A3,VF_st.OL2_2), new VF_trans(VF_act.NONE,VF_st.OL2_3B));		
		FsmMap.put( VF_tag(VF_ev.A2,VF_st.OL2_3A), new VF_trans(VF_act.NONE,VF_st.UP_1));		
		FsmMap.put( VF_tag(VF_ev.A1,VF_st.OL2_3B), new VF_trans(VF_act.DN_SAVE,VF_st.UP_2));
		FsmMap.put( VF_tag(VF_ev.B1,VF_st.OL2_3B), new VF_trans(VF_act.UP_SAVE,VF_st.OL2_5));		
		FsmMap.put( VF_tag(VF_ev.A1,VF_st.OL2_5), new VF_trans(VF_act.DN_SAVE,VF_st.IDLE));		
		FsmMap.put( VF_tag(VF_ev.B1,VF_st.CC_1), new VF_trans(VF_act.NONE,VF_st.CC_2));		
		FsmMap.put( VF_tag(VF_ev.A2,VF_st.CC_2), new VF_trans(VF_act.NONE,VF_st.CC_3));		
		FsmMap.put( VF_tag(VF_ev.A1,VF_st.CC_3), new VF_trans(VF_act.DN_SAVE,VF_st.UP_2));
		FsmMap.put( VF_tag(VF_ev.A2,VF_st.CC_3), new VF_trans(VF_act.DN_SAVE,VF_st.UP_2));
		FsmMap.put( VF_tag(VF_ev.A3,VF_st.CC_3), new VF_trans(VF_act.DN_SAVE,VF_st.UP_2));
		FsmMap.put( VF_tag(VF_ev.A4,VF_st.CC_3), new VF_trans(VF_act.DN_SAVE,VF_st.UP_2));
		FsmMap.put( VF_tag(VF_ev.B1,VF_st.CC_3), new VF_trans(VF_act.UP_SAVE,VF_st.DOWN_2));
		FsmMap.put( VF_tag(VF_ev.A1,VF_st.RESYNC), new VF_trans(VF_act.DN_NEW,VF_st.DOWN_1));
		FsmMap.put( VF_tag(VF_ev.A2,VF_st.RESYNC), new VF_trans(VF_act.DN_NEW,VF_st.DOWN_1));
		FsmMap.put( VF_tag(VF_ev.A3,VF_st.RESYNC), new VF_trans(VF_act.DN_NEW,VF_st.DOWN_1));
		FsmMap.put( VF_tag(VF_ev.A4,VF_st.RESYNC), new VF_trans(VF_act.DN_NEW,VF_st.DOWN_1));
		FsmMap.put( VF_tag(VF_ev.NA,VF_st.RESYNC), new VF_trans(VF_act.NONE,VF_st.RESYNC));

		/*
		 * I would expect the following code to return the same thing for temp and temp2
		 * but it generates different Hashes so only the original tag Object finds a match
		 * 
				VF_tag tTag = new VF_tag(VF_ev.NA,VF_st.RESYNC);
				FsmMap.put( tTag, new VF_trans(VF_act.NONE,VF_st.RESYNC));
				VF_trans temp = FsmMap.get( tTag);
				VF_tag tTag2 = new VF_tag(VF_ev.A4,VF_st.RESYNC);
				VF_trans temp2 = FsmMap.get( tTag2);
		 */

	}

	// make a String key from an event and a state
	private String VF_tag( VF_ev e, VF_st s) {
		return e.name() +":"+ s.name();
	}
	
	/*
	 * -------------------------------------------------------
	 * the FSM engine
	 */
	private VF_st FSM_fire ( VF_ev event, VF_st state ) {
		// fetch the FSM entry if valid
		VF_trans transition = FsmMap.get( VF_tag( event, state ));
		
		// execute actions for valid events
		if ( transition != null ) {
			switch( transition.action ) {
			case DN_NEW:
				carDn.setStart( day, timeStamp );
				break;
			case DN_SAVE:
				saveVehicle( carDn, timeStamp, downList);
				break;
			case UP_NEW:
				carUp.setStart( day, timeStamp );
				break;
			case UP_SAVE:
				saveVehicle( carUp, timeStamp, upList);
				break;
			case NONE:
				break;
			}
			return transition.next;
		}
		
		// report invalid events and start a RESYNC sequence
		System.out.println("FSM_fire: Unexpected event.");
		return VF_st.RESYNC;
	}
	
	/* -----------------------------------------------------
	*  Vehicle factory logic - analyse the sequence of Crossing events
	*	and build lists of vehicles travelling UP or DOWN
	*/
	public void analyse( Crossing next ) {

		sensor = next.getSensor();
		timeStamp = next.getTime();
		if (timeStamp < prevTime) day +=1;
		deltaT = TimeDiff.timeDiff(timeStamp, prevTime);
		prevTime = timeStamp;
		
		// fire event into the FSM
		state = FSM_fire( getEvent(), state );

	}

	/* -----------------------------------------------------
	* implement a singleton VehicleFactory instance
	*/
	public static VehicleFactory getInstance() { return INSTANCE; }
	
	public static final VehicleFactory INSTANCE = new VehicleFactory();
	
	private VehicleFactory(){
		// set up the FSM transition Map
		setupFsmMap();
		
		// Create empty VehicleLists, Up and Down
		upList = new LinkedList<Vehicle>();
		downList = new LinkedList<Vehicle>();
		System.out.println("Construct VehicleFactory");
		
		// initialise VF state info
		day = 1;
		prevTime = 0;
		carUp = new Vehicle(day,0,0,true);
		carDn = new Vehicle(day,0,0,false);
		state = VF_st.RESYNC;
	}
	
}
