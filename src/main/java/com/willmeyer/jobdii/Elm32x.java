package com.willmeyer.jobdii;

import com.willmeyer.jrs232.*;
import com.willmeyer.util.*;

import java.io.*;

import org.slf4j.*;

public class Elm32x extends Rs232Device implements Obd2Reader  {

	public Elm32x(String comPortName) throws Exception {
		super(comPortName, 38400, false);
	}
	
	protected static final byte[] ATZ = {'A', 'T', 'Z', '\r', '\n'}; // init
	protected static final byte[] ATE0 = {'A', 'T', 'E', '0','\r', '\n'}; // echo off
	protected static final byte[] ATL0 = {'A', 'T', 'L', '0','\r', '\n'}; // linefeeds off
	protected static final byte[] ATH0 = {'A', 'T', 'H', '0','\r', '\n'}; // headers off
	protected static final byte[] INIT = {'0', '1', '0', '0','\r', '\n'}; // init obdii/can connection
	
	protected final Logger logger = LoggerFactory.getLogger(Elm32x.class);
	
	@Override
	/**
	 * Adds init logic to the basic connect process.
	 */
	public void connect() throws Exception {
		super.connect();
		this.initComms();
	}
	
	public Number getPid(int mode, Pid pid) throws Exception {
		byte[] raw = this.getRawPid(mode, pid.pid);
		logger.debug("About to give raw PID bytes to a formula for calculation.  Bytes are: {}", Bytes.debugBytes(raw));
		Number calculated = pid.formula.calculateValue(raw);
		logger.debug("Calculated PID is {}", calculated);
		return calculated;
	}
	
	public byte[] getRawPid(int mode, byte pid) throws Exception {
		logger.debug("Writing PID fetch command");
		String command = Bytes.byteAsText((byte)mode) + Bytes.byteAsText(pid) + "\r\n";
		this.sendBytes(command.getBytes("utf-8"));
		logger.debug("Reading response...");
		byte[] respBytes = this.readToPrompt();
		logger.debug("Got PID response data: {}", Bytes.debugBytes(respBytes));
		
		// The response looks like MODEBYTE SPACE PIDBYTE SPACE DATABYTE SPACE [repeat] CR CR 
		// "41 0D 00 \r \r
		String respStr = new String(respBytes, "us-ascii");
		logger.debug("respinse as string: {}", respStr);
		if (respStr.contains("ERROR")) {
			throw new Exception("Error communicating with vehicle (" + respStr + ")");
		}
		int numDataBytes = respBytes.length - 8; // header and trailer, with N number of DATABYTE SPACE 3-byte-sequences left
		numDataBytes = numDataBytes / 3;
		byte[] pidBytes = new byte[numDataBytes];
		for (int i = 0; i < numDataBytes; i++) {
			char digit1 = (char)respBytes[6 + i*3];
			char digit2 = (char)respBytes[6 + i*3 + 1];
			logger.debug("Digits: {}{}", digit1, digit2);
			String byteStr = "" + digit1 + digit2;
			logger.debug("Byte str: {}", byteStr);
			pidBytes[i] = (byte)Integer.parseInt(byteStr, 16);
		}
		logger.debug("Stripped down to {} data bytes: {}", numDataBytes, Bytes.debugBytes(pidBytes));
		return pidBytes; 
	}

	private byte[] readToPrompt() throws Exception {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		int b;
		while ((b = this.inStream.read()) != -1) {
			if (b == '>') break;
			buf.write(b);
		}
		byte[] bytesRead = buf.toByteArray(); 
		logger.debug("Read bytes: {}", Bytes.debugBytes(bytesRead));
		return bytesRead;
	}

	protected void initComms() throws Exception {
		
		// Reset device
		this.sendBytes(ATZ);
		Thread.sleep(200);
		readToPrompt();
		
		// Turn off echos, linefeeds, headers
		this.sendBytes(ATE0);
		readToPrompt();
		//this.sendBytes(ATL0);
		//Thread.sleep(200);
		this.sendBytes(ATH0);
		readToPrompt();
		
		this.sendBytes(INIT);
		byte[] resp = readToPrompt();
		String respStr = new String(resp, "us-ascii");
		if (respStr.contains("ERROR")) {
			throw new Exception ("OBDII bus not connected (" + respStr + ")");
		}
	}
}
