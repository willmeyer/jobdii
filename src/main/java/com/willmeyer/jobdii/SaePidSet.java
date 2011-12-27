package com.willmeyer.jobdii;

import org.slf4j.*;

import com.willmeyer.util.Bytes;

/**
 * http://en.wikipedia.org/wiki/Table_of_OBD-II_Codes
 */
public final class SaePidSet extends PidSet {

	private static SaePidSet theSet = null;
	
	private final Logger logger = LoggerFactory.getLogger(SaePidSet.class);
	
	public static SaePidSet theSet() {
		if (theSet == null) {
			theSet = new SaePidSet();
		}
		return theSet;
	}
	
	private SaePidSet() {
		super("sae");
		Pid pid;
		
		// mph
		pid = new Pid("mph", "speed", (byte)0x0d, "mph", new Pid.Formula() { 
			public Number calculateValue(byte[] byteVal) {
				// formula: A / 0.62
				int kph = Bytes.toInt(byteVal[0]);
				int mph = (int)((float)kph / 0.62);
				return new Integer(mph);
				}
			});
		this.pids.put("mph", pid);

		// kph
		pid = new Pid("kph", "speed (kph)", (byte)0x0d, "kph", new Pid.SingleByteInteger());
		this.pids.put("kph", pid);
		
		// maf
		pid = new Pid("maf", "mass air flow", (byte)0x10, "g/s", new Pid.Formula() { 
			public Number calculateValue(byte[] byteVal) {
				// formula: ((256*A)+B) / 100
				int maf = ((256*Bytes.toInt(byteVal[0])) + Bytes.toInt(byteVal[1])) / 100;
				return new Integer(maf);
				}
			});
		this.pids.put("maf", pid);
		
		// rpm
		pid = new Pid("rpm", "engine speed", (byte)0x0c, "rpm", new Pid.Formula() { 
			public Number calculateValue(byte[] byteVal) {
				// formula: ((A*256)+B)/4
				int rpm = ((Bytes.toInt(byteVal[0]) * 256) + Bytes.toInt(byteVal[1])) / 4;
				return new Integer(rpm);
				}
			});
		this.pids.put("rpm", pid);

		// fuel
		pid = new Pid("fuel", "fuel level", (byte)0x2f, "%", new Pid.Formula() { 
			public Number calculateValue(byte[] byteVal) {
				// formula: 100*A/255
				int fuel = 100 * Bytes.toInt(byteVal[0]) / 255;
				return new Integer(fuel);
				}
			});
		this.pids.put("fuel", pid);

		// ect
		pid = new Pid("ect", "coolant temperature", (byte)0x05, "deg F", new Pid.Formula() { 
			public Number calculateValue(byte[] byteVal) {
				// formula: A-40
				logger.debug("Calculating ECT value, byte is {}", Bytes.toInt(byteVal[0]));
				int ect = Bytes.toInt(byteVal[0]) - 40;
				
				// Now go to Fahrenheit
				ect = (int)((9.0/5.0) * ect + 32);
				return new Integer(ect);
				}
			});
		this.pids.put("ect", pid);
	}
}
