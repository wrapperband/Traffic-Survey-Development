package vehicles;

public class VF_trans {
	public VF_act action;
	public VF_st  next;
	
	public VF_trans ( VF_act a, VF_st s ) {
		action = a;
		next = s;
	}
}
