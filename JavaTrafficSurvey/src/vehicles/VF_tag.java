package vehicles;

/*
 *  NOTE: this class is not being used in the current implementation
 *  	The Map<K,V> interface does not correctly support using a VF_Tag Object as a Key
 *  	It uses a Hash which does not 'equal' the Hash of an new identical tag 
 *  	VehicleFactory instead generates a String using the method VF_Tag()
 */

public class VF_tag {
	public VF_ev event;
	public VF_st state;
	
	public boolean equals( VF_tag t) {
		return (t.event == event && t.state == state);
	}
	
	public VF_tag ( VF_ev e, VF_st s ) {
		event = e;
		state = s;
	}
}
