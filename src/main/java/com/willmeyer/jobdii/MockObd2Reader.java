package com.willmeyer.jobdii;

public class MockObd2Reader implements Obd2Reader {

	public Number getPid(int mode, Pid pid) {
		Integer res = new Integer(0);
		switch (pid.pid) {
		case 0x0D: // mph
			res = 46;
			break;
		case 0x05: // ect
			res = 186;
			break;
		case 0x10: // maf
			res = 45;
			break;
		case 0x0C: // rpm
			res = 3674;
			break;
		case 0x2f: // fuel
			res = 87;
			break;
		default:
			res = 0;
			break;
		}
		return res;
	}
	
	public byte[] getRawPid(int mode, byte pid) {
		byte[] raw = {0x00, 0x00};
		return raw;
	}

}
