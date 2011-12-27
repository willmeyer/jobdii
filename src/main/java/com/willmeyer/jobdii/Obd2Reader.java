package com.willmeyer.jobdii;

/**
 * The basic communications interface to an OBDII interface.
 */
public interface Obd2Reader {

	/**
	 * Gets the value for a given PID, using the appropriate formula.
	 * 
	 * @param mode
	 * @param pid
	 * @return
	 */
	public Number getPid(int mode, Pid pid) throws Exception;
	
	/**
	 * Requests the PID using its byte representation, and returns the value in its byte 
	 * representation.
	 * 
	 * @param mode
	 * @param pid
	 * @return
	 */
	public byte[] getRawPid(int mode, byte pid) throws Exception;
}
