package mazePD;

import java.util.Stack;


public class Droid implements DroidInterface {

	String name;
	
	public Droid() {
		this.name = "";
		
	}
	
	public Droid(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

}

