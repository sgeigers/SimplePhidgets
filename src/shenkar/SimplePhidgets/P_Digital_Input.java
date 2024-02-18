package shenkar.SimplePhidgets;

import processing.core.*;
import java.lang.reflect.Method;
import com.phidget22.*;

public class P_Digital_Input extends Device {
	// event
	Method stateChangeEventMethod;  // stateChange
	boolean stateChangeFlag = false;
	boolean stateChangeEventReportChannel = false;

	// real-time event
	Method stateChangeEventRTMethod;  // stateChangeRT
	boolean RTEventRegister = false;

	public P_Digital_Input(PApplet P5Parent, Channel ChParent, String type, int serialNum, int portNum, int chNum) {
		super(P5Parent, ChParent, type, serialNum, portNum, chNum);

		// pre-opening setup
		try {
			device = new DigitalInput();
		}	catch (PhidgetException ex) {
			System.err.println("Could not open device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
			PAppletParent.exit();
		}

		// device opening
		switch (deviceType) {
		case "DAQ1200": // 4x Digital Input Phidget
		case "DAQ1300": // 4x Isolated Digital Input Phidget
		case "DAQ1301": // 16x Isolated Digital Input Phidget
		case "DAQ1400": // Versatile Input Phidget
		case "HIN1100": // Thumbstick Phidget
		case "HIN1101": // Dial Phidget

			init(false);
			break;

		case "1010": // PhidgetInterfaceKit 8/8/8
		case "1011": // PhidgetInterfaceKit 2/2/2
		case "1012": // PhidgetInterfaceKit 0/16/16
		case "1013": // PhidgetInterfaceKit 8/8/8
		case "1018": // PhidgetInterfaceKit 8/8/8
		case "1019": // PhidgetInterfaceKit 8/8/8
		case "1047": // PhidgetEncoder HighSpeed 4-Input 
		case "1052": // PhidgetEncoder
		case "1063": // PhidgetStepper Bipolar 1-Motor
		case "1065": // PhidgetMotorControl 1-Motor
		case "1070": // PhidgetSBC
		case "1072": // PhidgetSBC2
		case "1073": // PhidgetSBC3
		case "1202": // PhidgetTextLCD 20X2 : Blue : Integrated PhidgetInterfaceKit 8/8/8
		case "1203": // PhidgetTextLCD 20X2 : White : Integrated PhidgetInterfaceKit 8/8/8
		case "1219": // PhidgetTextLCD 20X2 White with PhidgetInterfaceKit 0/8/8
		case "1220": // PhidgetTextLCD 20X2 Blue with PhidgetInterfaceKit 0/8/8
		case "1221": // PhidgetTextLCD 20X2 Green with PhidgetInterfaceKit 0/8/8
		case "1222": // PhidgetTextLCD 20X2 Red with PhidgetInterfaceKit 0/8/8
			initNoHub();
			break;

		case "HUB0000": // 6-Port USB VINT Hub Phidget
		case "HUB0001": // 6-Port USB VINT Hub Phidget
		case "HUB0002": // 6-Port USB VINT Hub Phidget
		case "HUB0007": // 1-Port USB VINT Hub Phidget
		case "HUB5000": // 6-Port Network VINT Hub Phidget
		case "SBC3003": // PhidgetSBC4 - 6-Port VINT Hub Phidget
		default:
			init(true);
			break;
		}

		// post-opening setup
		attachListeners();
	}

	// check if "stateChange()" was defined in the sketch and create a listener for it.
	void attachListeners() {
		// stateChange()
		try {
			stateChangeEventMethod =  PAppletParent.getClass().getMethod("stateChange");
			if (stateChangeEventMethod != null) {
				stateChangeEventReportChannel = false;
				((DigitalInput)device).addStateChangeListener(new DigitalInputStateChangeListener() {
					public void onStateChange(DigitalInputStateChangeEvent e) {
						//System.out.println(e.toString());
						stateChangeFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "stateChange" not defined
		}

		// stateChange(Channel)
		try {
			stateChangeEventMethod =  PAppletParent.getClass().getMethod("stateChange", new Class<?>[] { Channel.class });
			if (stateChangeEventMethod != null) {
				stateChangeEventReportChannel = true;
				((DigitalInput)device).addStateChangeListener(new DigitalInputStateChangeListener() {
					public void onStateChange(DigitalInputStateChangeEvent e) {
						//System.out.println(e.toString());
						stateChangeFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "stateChange" not defined
		}

		// stateChangeRT()
		try {
			stateChangeEventRTMethod =  PAppletParent.getClass().getMethod("stateChangeRT");
			if (stateChangeEventRTMethod != null) {
				if (stateChangeEventMethod != null) {
					System.err.println("Cannot use both stateChange() and stateChangeRT()."); 
				}
				else {
					RTEventRegister = true;
					stateChangeEventReportChannel = false;
				}
			}
		} catch (Exception e) {
			// function "stateChangeRT()" not defined
		}

		// stateChangeRT(Channel)
		try {
			stateChangeEventRTMethod =  PAppletParent.getClass().getMethod("stateChangeRT", new Class<?>[] { Channel.class });
			if (stateChangeEventRTMethod != null) {
				if (stateChangeEventMethod != null) {
					System.err.println("Cannot use both stateChange() and stateChangeRT()."); 
				}
				else {
					RTEventRegister = true;
					stateChangeEventReportChannel = true;
				}
			}
		} catch (Exception e) {
			// function "stateChangeRT()" not defined
		}
	}

	public void pre() {
		if (RTEventRegister) {
			RTEventRegister = false;
			try {
				if (stateChangeEventReportChannel) { // stateChangeRT(Channel)
					((DigitalInput)device).addStateChangeListener(new DigitalInputStateChangeListener() {
						public void onStateChange(DigitalInputStateChangeEvent e) {
							//System.out.println(e.toString());
							try {
								if (stateChangeEventRTMethod != null) {
									stateChangeEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
								}
							} catch (Exception ex) {
								System.err.println("Disabling stateChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								stateChangeEventRTMethod = null;
							}
						}
					});
				}
				else { // stateChangeRT()
					((DigitalInput)device).addStateChangeListener(new DigitalInputStateChangeListener() {
						public void onStateChange(DigitalInputStateChangeEvent e) {
							//System.out.println(e.toString());
							try {
								if (stateChangeEventRTMethod != null) {
									stateChangeEventRTMethod.invoke(PAppletParent);
								}
							} catch (Exception ex) {
								System.err.println("Disabling stateChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								stateChangeEventRTMethod = null;
							}
						}
					});
				}
			} catch (Exception ex) {
		    	System.err.println("Disabling stateChangeRT() for " + deviceType + " because of an error:");
		    	ex.printStackTrace();
		    	stateChangeEventRTMethod = null;
		    }
		}
	}
	
	/**
	 * handles events. Do not call.
	 * 
	 */	
	@Override
	public void draw() {
		if (stateChangeFlag) {
			stateChangeFlag = false;
			try {
				if (stateChangeEventMethod != null) {
					if (stateChangeEventReportChannel) {
						stateChangeEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						stateChangeEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling stateChange() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				stateChangeEventMethod = null;
			}
		}
	}

	/*
	 * most basic way to use the channel. returns 0 or 1.
	 * 
	 * @return int
	 */
	@Override
	public int read() {
		try {
			if (((DigitalInput)device).getState())
				return 1;
			else
				return 0;
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get state of device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0; 
	}

	/*
	 * for some devices, returns whether input mode is NPN or PNP
	 * 
	 * @return String
	 */
	@Override
	public String getInputMode() {  // NPN or PNP
		if (deviceType ==  "DAQ1400") {
			try {
				InputMode im = (((DigitalInput)device).getInputMode());
				switch (im) {
				case NPN:
					return "NPN";
				case PNP:
					return "PNP";
				}
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get input mode from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getInputMode() is not valid for device of type " + deviceType);				
		}
		return "";
	}

	/*
	 * for some devices, sets whether input mode is NPN or PNP
	 * 
	 * @param im input mode as String ("NPN" or "PNP")
	 */
	@Override
	public void setInputMode(String im) {
		if (deviceType ==  "DAQ1400") {
			try { 
				InputMode i = InputMode.NPN;
				switch (im) {
				case "PNP":
					i = InputMode.PNP;
					break;
				case "NPN":
					break;
				default:
					System.err.println("Invalid input mode: " + im + ". Use only \"NPN\" or \"PNP\"");
					break;						
				}
				((DigitalInput)device).setInputMode(i);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set input mode for device " + deviceType);
			}
		}
		else {
			System.err.println("setInputMode(String) is not valid for device of type " + deviceType);				
		}
	}

	/*
	 * for some devices, returns power supply in Volts: 12, 24 or 0 for OFF
	 * 
	 * @return int
	 */
	@Override
	public int getPowerSupply() {  // in V; 12, 24 or 0 for OFF
		if (deviceType ==  "DAQ1400") {
			try {
				PowerSupply ps = (((DigitalInput)device).getPowerSupply());
				switch (ps) {
				case OFF:
					return 0;
				case VOLTS_12:
					return 12;
				case VOLTS_24:
					return 24;
				}
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get power supply from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getPowerSupply() is not valid for device of type " + deviceType);				
		}
		return 0;
	}

	/*
	 * for some devices, sets power supply in Volts: 12, 24 or 0 for OFF
	 * 
	 * @param ps power supply in volts - 0, 12 or 24
	 */
	@Override
	public void setPowerSupply(int ps) {
		if (deviceType ==  "DAQ1400") {
			try {
				PowerSupply p = PowerSupply.OFF;
				switch (ps) {
				case 12:
					p = PowerSupply.VOLTS_12;
					break;
				case 24:
					p = PowerSupply.VOLTS_24;
					break;
				case 0:
					break;
				default:
					System.err.println("Invalid power supply: " + ps + ". Use power supply type in Volts, and only 12, 24 or 0 to turn off");
					break;						
				}
				((DigitalInput)device).setPowerSupply(p);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set power supply to device " + deviceType);
			}
		}
		else {
			System.err.println("setPowerSupply(int) is not valid for device of type " + deviceType);				
		}
	}

	/*
	 * get state sensed by the device as boolean
	 * 
	 * @return boolean
	 */
	@Override
	public boolean getState() {
		try {
			return ((DigitalInput)device).getState();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get state of device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return false; 
	}
}
