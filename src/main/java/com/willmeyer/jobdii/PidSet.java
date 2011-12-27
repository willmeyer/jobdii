package com.willmeyer.jobdii;

import java.util.*;

public class PidSet {

	protected HashMap<String, Pid> pids;
	protected String setName;
	
	public PidSet(String setName) {
		this.setName = setName.toLowerCase();
		pids = new HashMap<String, Pid>();
	}
	
	public Pid getPid(String pidName) {
		return pids.get(pidName.toLowerCase());
	}
	
	public String getName() {
		return this.setName;
	}

	public Set<String> getPidNames() {
		return pids.keySet();
	}
}
