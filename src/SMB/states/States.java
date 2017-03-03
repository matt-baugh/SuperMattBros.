package SMB.states;

public class States {
	//keep all the ID's of the states here, so they can easily be checked
	//and to make sure there are no duplicated ID's
	//the relevant one of these is called by any state when it runs its getID() method
	public static final int MENU = 0;
	public static final int LOCALGAME = 1;
	public static final int SERVER = 2;
	public static final int CLIENT = 3;
	public static final int IPINPUT = 4;
	public static final int GAMECOORDINATOR = 5;
	public static final int SERVERTYPECHOOSER = 6;

}
