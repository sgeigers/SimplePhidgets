package shenkar.SimplePhidgets;

import processing.core.*;
import java.lang.reflect.Method;
import com.phidget22.*;

public class P_Encoder extends Device {
	// event
	Method positionChangeEventMethod;  // positionChange
	boolean positionChangeFlag = false;
	boolean positionChangeEventReportChannel = false;

	// real-time event
	Method positionChangeEventRTMethod;  // positionChangeRT
	boolean RTEventRegister = false;

	public P_Encoder(PApplet P5Parent, Channel ChParent, String type, int serialNum, int portNum, int chNum) {
		super(P5Parent, ChParent, type, serialNum, portNum, chNum);

		// pre-opening setup
		try {
			device = new Encoder();
		}	catch (PhidgetException ex) {
			System.err.println("Could not open device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
			PAppletParent.exit();
		}

		// device opening
		switch (deviceType) {
		case "DCC1000": // DC Motor Phidget
		case "DCC1001": // 2A DC Motor Phidget
		case "DCC1002": // 4A DC Motor Phidget
		case "ENC1000": // Quadrature Encoder Phidget
		case "HIN1101": // Dial Phidget
			init(false);
			break;

		case "1047": // PhidgetEncoder HighSpeed 4-Input
		case "1052": // PhidgetEncoder
		case "1057": // PhidgetEncoder HighSpeed
		case "1065": // PhidgetMotorControl 1-Motor
			initNoHub();
			break;
		}

		// post-opening setup
		try {
			((Encoder)device).setDataInterval(((Encoder)device).getMinDataInterval());			
		}	catch (PhidgetException ex) {
			System.err.println("Could not set data interval for device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
		}
		attachListeners();
	}

	// check if "positionChange()" was defined in the sketch and create a listener for it.
	void attachListeners() {
		// positionChange()
		try {
			positionChangeEventMethod =  PAppletParent.getClass().getMethod("positionChange");
			if (positionChangeEventMethod != null) {
				positionChangeEventReportChannel = false;
				((Encoder)device).addPositionChangeListener(new EncoderPositionChangeListener() {
					public void onPositionChange(EncoderPositionChangeEvent e) {
						//System.out.println(e.toString());
						positionChangeFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "positionChange" not defined
		}

		// positionChange(Channel)
		try {
			positionChangeEventMethod =  PAppletParent.getClass().getMethod("positionChange", new Class<?>[] { Channel.class });
			if (positionChangeEventMethod != null) {
				positionChangeEventReportChannel = true;
				((Encoder)device).addPositionChangeListener(new EncoderPositionChangeListener() {
					public void onPositionChange(EncoderPositionChangeEvent e) {
						//System.out.println(e.toString());
						positionChangeFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "positionChange(Channel)" not defined
		}

		// positionChangeRT()
		try {
			positionChangeEventRTMethod =  PAppletParent.getClass().getMethod("positionChangeRT");
			if (positionChangeEventRTMethod != null) {
				if (positionChangeEventMethod != null) {
					System.err.println("Cannot use both positionChange() and positionChangeRT()."); 
				}
				else {
					RTEventRegister = true;
					positionChangeEventReportChannel = false;
				}
			}
		} catch (Exception e) {
			// function "positionChangeRT()" not defined
		}

		// positionChangeRT(Channel)
		try {
			positionChangeEventRTMethod =  PAppletParent.getClass().getMethod("positionChangeRT", new Class<?>[] { Channel.class });
			if (positionChangeEventRTMethod != null) {
				if (positionChangeEventMethod != null) {
					System.err.println("Cannot use both positionChange() and positionChangeRT()."); 
				}
				else {
					RTEventRegister = true;
					positionChangeEventReportChannel = true;
				}
			}
		} catch (Exception e) {
			// function "positionChangeRT(Channel)" not defined
		}
	}

	public void pre() {
		if (RTEventRegister) {
			RTEventRegister = false;
			try {
				if (positionChangeEventReportChannel) { // positionChangeRT(Channel)
					((Encoder)device).addPositionChangeListener(new EncoderPositionChangeListener() {
						public void onPositionChange(EncoderPositionChangeEvent e) {
							//System.out.println(e.toString());
							try {
								if (positionChangeEventRTMethod != null) {
									positionChangeEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
								}
							} catch (Exception ex) {
								System.err.println("Disabling positionChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								positionChangeEventRTMethod = null;
							}
						}
					});
				}
				else { // positionChangeRT()
					((Encoder)device).addPositionChangeListener(new EncoderPositionChangeListener() {
						public void onPositionChange(EncoderPositionChangeEvent e) {
							//System.out.println(e.toString());
							try {
								if (positionChangeEventRTMethod != null) {
									positionChangeEventRTMethod.invoke(PAppletParent);
								}
							} catch (Exception ex) {
								System.err.println("Disabling positionChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								positionChangeEventRTMethod = null;
							}
						}
					});
				}
			} catch (Exception ex) {
		    	System.err.println("Disabling positionChangeRT() for " + deviceType + " because of an error:");
		    	ex.printStackTrace();
		    	positionChangeEventRTMethod = null;
		    }
		}
	}
	
	/**
	 * handles events. Do not call.
	 * 
	 */	
	@Override
	public void draw() {
		if (positionChangeFlag) {
			positionChangeFlag = false;
			try {
				if (positionChangeEventMethod != null) {
					if (positionChangeEventReportChannel) {
						positionChangeEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						positionChangeEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling positionChange() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				positionChangeEventMethod = null;
			}
		}
	}

	/*
	 * most basic way to use the channel. returns position as int.
	 * 
	 * @return int
	 */
	@Override
	public int read() {
		try {
			return (int)(((Encoder)device).getPosition());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get position of device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0; 
	}

	@Override
	public boolean getEnabled() {
		try {
			return ((Encoder)device).getEnabled();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get enabled state from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return false;
	}

	@Override
	public void setEnabled(boolean en) {  // ENC1000 HIN1101 1047 1057(only for getDeviceVersion >=400)
		boolean valid = false;
		if (deviceType.equals("ENC1000") || deviceType.equals("HIN1101") || deviceType.equals("1047")) valid = true;
		else if (deviceType.equals("1057")) {
			try {
				if (device.getDeviceVersion() >= 400) valid = true;
				else {
					System.err.println("Set enabled state not valid to device type 1057 with firmware version lower than 400");
					PAppletParent.exit();
				}
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get firmware version of device " + deviceType + " because of error: " + ex);
				PAppletParent.exit();
			}
		}
		if (valid) {
			try {
				((Encoder)device).setEnabled(en);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set enabled state to device " + deviceType + " because of error: " + ex);
				PAppletParent.exit();
			}
		}
		else {
			System.err.println("setEnabled(boolean) is not valid for device of type " + deviceType);	
		}
	}

	@Override
	public int getDataInterval() {
		try {
			return ((Encoder)device).getDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setDataInterval(int dataInterval) {
		try {
			((Encoder)device).setDataInterval(dataInterval);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set data interval value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public int getMinDataInterval() {
		try {
			return ((Encoder)device).getMinDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public int getMaxDataInterval() {
		try {
			return ((Encoder)device).getMaxDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public long getIndexPosition() {
		boolean valid = false;
		switch (deviceType) {
			case "DCC1000": // DC Motor Phidget
			case "DCC1001": // 2A DC Motor Phidget
			case "DCC1002": // 4A DC Motor Phidget
			case "ENC1000": // Quadrature Encoder Phidget
			case "1047": // PhidgetEncoder HighSpeed 4-Input
			case "1065": // PhidgetMotorControl 1-Motor
				valid = true;
				break;

			case "1057": // PhidgetEncoder HighSpeed
				try {
					if (device.getDeviceVersion() >= 400) valid = true;
					else {
						System.err.println("Get index position not valid to device type 1057 with firmware version lower than 400");
						PAppletParent.exit();
					}
				}
				catch (PhidgetException ex) {
					System.err.println("Cannot get firmware version of device " + deviceType + " because of error: " + ex);
					PAppletParent.exit();
				}
				break;

			default:
				break;
		}
		
		if (valid) {
			try {
				return ((Encoder)device).getIndexPosition();
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get index position value from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getIndexPosition() is not valid for device of type " + deviceType);							
		}
		return 0;
	}
	
	@Override
	public String getIOMode() { 
		boolean valid = false;
		if ((deviceType.equals("ENC1000")) || (deviceType.equals("DCC1000"))) valid = true;
		else if (deviceType == "1057") {
			try {
				if (device.getDeviceVersion() >= 400) valid = true;
				else {
					System.err.println("Get interface mode not valid to device type 1057 with firmware version lower than 400");
					PAppletParent.exit();
				}
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get firmware version of device " + deviceType + " because of error: " + ex);
				PAppletParent.exit();
			}
		}
		if (valid) {
			try {
				EncoderIOMode em = (((Encoder)device).getIOMode());
				switch (em) {
				case PUSH_PULL:
					return "PushPull";
				case LINE_DRIVER_2K2:
					return "LineDriver_2K2";
				case LINE_DRIVER_10K:
					return "LineDriver_10K";
				case OPEN_COLLECTOR_2K2:
					return "OpenCollector_2K2";
				case OPEN_COLLECTOR_10K:
					return "OpenCollector_10K";
				}
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get interface mode from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getIOMode() is not valid for device of type " + deviceType);				
		}
		return "";
	}

	@Override
	public void setIOMode(String em) {
		boolean valid = false;
		if ((deviceType.equals("ENC1000")) || (deviceType.equals("DCC1000"))) valid = true;
		else if (deviceType == "1057") {
			try {
				if (device.getDeviceVersion() >= 400) valid = true;
				else {
					System.err.println("Set interface mode not valid to device type 1057 with firmware version lower than 400");
					PAppletParent.exit();
				}
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get firmware version of device " + deviceType + " because of error: " + ex);
				PAppletParent.exit();
			}
		}
		if (valid) {
			try { 
				EncoderIOMode i = EncoderIOMode.PUSH_PULL;
				switch (em.toUpperCase()) {
				case "PUSHPULL":
				case "PUSH_PULL":
					break;

				case "LINEDRIVER2K2":
				case "LINEDRIVER_2K2":
				case "LINE_DRIVER_2K2":
					i = EncoderIOMode.LINE_DRIVER_2K2;
					break;

				case "LINEDRIVER10K":
				case "LINEDRIVER_10K":
				case "LINE_DRIVER_10K":
					i = EncoderIOMode.LINE_DRIVER_10K;
					break;
					
				case "OPENCOLLECTOR2K2":
				case "OPENCOLLECTOR_2K2":
				case "OPEN_COLLECTOR_2K2":
					i = EncoderIOMode.OPEN_COLLECTOR_2K2;
					break;

				case "OPENCOLLECTOR10K":
				case "OPENCOLLECTOR_10K":
				case "OPEN_COLLECTOR_10K":
					i = EncoderIOMode.OPEN_COLLECTOR_10K;
					break;

				default:
					System.err.println("Invalid interface mode: " + em + ". Use only \"PushPull\", \"LineDriver_2K2\", \"LineDriver_10K\", \"OpenCollector_2K2\" or \"OpenCollector_10K\"");
					break;						
				}
				((Encoder)device).setIOMode(i);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set interface mode for device " + deviceType);
			}
		}
		else {
			System.err.println("setIOMode(String) is not valid for device of type " + deviceType);				
		}
	}

	@Override
	public long getEncPosition() {
		try {
			return ((Encoder)device).getPosition();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get encoder position from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}
	
	@Override
	public void setEncPosition(long pos	) {
		try {
			((Encoder)device).setPosition(pos);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set encoder position to device " + deviceType + " because of error: " + ex);
		}
	}
	
	@Override
	public int getPositionChangeTrigger() {
		try {
			return ((Encoder)device).getPositionChangeTrigger();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get position change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setPositionChangeTrigger(int trigger) {
		try {
			((Encoder)device).setPositionChangeTrigger(trigger);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set position change trigger for device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public int getMinPositionChangeTrigger() {
		try {
			return ((Encoder)device).getMinPositionChangeTrigger();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get minimum position change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public int getMaxPositionChangeTrigger() {
		try {
			return ((Encoder)device).getMaxPositionChangeTrigger();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get maximum position change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}
}
