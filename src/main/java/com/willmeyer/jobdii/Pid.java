package com.willmeyer.jobdii;

/**
 * A specific OBDII quantity.  Has a 1-byte ID, a short name by which is van be looked up, and a 
 * long friendly name.
 * 
 * A PID also has a formula that is used to calculate its logical value from the actual bytes read from the car. 
 */
public class Pid {

	protected String shortName;
	protected String friendlyName;
	protected byte pid;
	protected String units;
	protected Formula formula;
	
	public Pid(String shortName, String friendlyName, byte pid, String units, Formula formula) {
		this.shortName = shortName.toLowerCase();
		this.friendlyName = friendlyName;
		this.pid = pid;
		this.units = units.toLowerCase();
		this.formula = formula;
	}
	
	public String getShortName() {
		return this.shortName;
	}
	
	public String getFriendlyName() {
		return this.friendlyName;
	}

	public String getUnits() {
		return this.units;
	}

	public static abstract class Formula {

		public abstract Number calculateValue(byte[] byteVal);
	}
	
	public static class SingleByteInteger extends Formula {

		@Override
		public Number calculateValue(byte[] byteVal) {
			return new Integer(byteVal[0]);
		}
		
	}

}
