package com.willmeyer.jobdii;

import java.util.*;

public class Obd2ReaderApp {

	/**
	 * Usage: [comport] <pidname,pidname,pidname>
	 * @param params
	 */
	public static void main(String[] params) {
		
		// Params
		if (params.length != 2) {
			usage();
			return;
		}
		String port = params[0];
		String pidList = params[1];
		String[] pids = pidList.split(",");
		
		// Do real work
		Elm32x elm = null;
		try {
			
			// Load up pid sets
			HashMap<String, PidSet> pidSets = new HashMap<String, PidSet>();
			PidSet sae = SaePidSet.theSet();
			pidSets.put(sae.setName, sae);
			
			// Init the device
			System.out.println("Initting device on port " + port);
			elm = new Elm32x(port);
			elm.connect();
			
			// Get and echo each pid
			for (String pidString : pids) {
				System.out.println("PID: " + pidString.toUpperCase());
				int dot = pidString.indexOf(".");
				String setName = pidString.substring(0, dot);
				String pidName = pidString.substring(dot+1);
				Pid pid = pidSets.get(setName).getPid(pidName);
				Number val = elm.getPid(1, pid);
				System.out.println("    " + val + " " + pid.units);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (elm != null) {
				elm.disconnect();
			}
		}
		
	}
	
	public static void usage() {
		System.out.println("USAGE: Ob2ReaderApp [comport] [pidname,pidname,...]");
	}
}
