package shenkar.SimplePhidgets;

import processing.core.*;
import java.lang.reflect.Method;
import com.phidget22.*;

public class P_Current_Input extends Device {
	// events
	Method currentChangeEventMethod;  // currentChange
	boolean currentChangeFlag = false;
	boolean currentChangeEventReportChannel = false;

	// real-time events
	Method currentChangeEventRTMethod;  // currentChangeRT
	boolean RTEventRegister = false;


	public P_Current_Input(PApplet P5Parent, Channel ChParent, String type, int serialNum, int portNum, int chNum) {
		super(P5Parent, ChParent, type, serialNum, portNum, chNum);

		// pre-opening setup
		try {
			device = new CurrentInput();
		}	catch (PhidgetException ex) {
			System.err.println("Could not open device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
			PAppletParent.exit();
		}
		
		// device opening
		switch (deviceType) {
		case "1061":  // PhidgetAdvancedServo 8-Motor
		case "1063":  // PhidgetStepper Bipolar 1-Motor
		case "1064":  // PhidgetMotorControl HC
		case "1065":  // PhidgetMotorControl 1-Motor
		case "1066":   // PhidgetAdvancedServo 1-Motor
			initNoHub();
			break;
			
		case "DAQ1400": // Versatile Input Phidget
		case "DCC1000": // DC Motor Phidget				
		case "VCP1100": // 30A Current Sensor Phidget
			init(false);
			break;
			
		default:
			init(true);
			break;
		}

		// post-opening setup
		try {
			// set maximum data rate as default, but not for MOT2002 - this may causes problems...
			((CurrentInput)device).setDataInterval(((CurrentInput)device).getMinDataInterval());
		}	catch (PhidgetException ex) {
			System.err.println("Could not set data interval and sensor type for device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
		}
		attachListeners();
	}

	// check if "currentChange()" or "currentChangeRT()" were defined in the sketch and create a listener for it.
	void attachListeners() {
		// currentChange()
		try {
			currentChangeEventMethod =  PAppletParent.getClass().getMethod("currentChange");
			if (currentChangeEventMethod != null) {
				currentChangeEventReportChannel = false;
				((CurrentInput)device).addCurrentChangeListener(new CurrentInputCurrentChangeListener() {
					public void onCurrentChange(CurrentInputCurrentChangeEvent e) {
						//System.out.println(e.toString());
						currentChangeFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "currentChange" not defined
		}

		// currentChange(Channel)
		try {
			currentChangeEventMethod =  PAppletParent.getClass().getMethod("currentChange", new Class<?>[] { Channel.class });
			if (currentChangeEventMethod != null) {
				currentChangeEventReportChannel = true;
				((CurrentInput)device).addCurrentChangeListener(new CurrentInputCurrentChangeListener() {
					public void onCurrentChange(CurrentInputCurrentChangeEvent e) {
						//System.out.println(e.toString());
						currentChangeFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "currentChange(Channel)" not defined
		}

		// currentChangeRT()
		try {
			currentChangeEventRTMethod =  PAppletParent.getClass().getMethod("currentChangeRT");
			if (currentChangeEventRTMethod != null) {
				if (currentChangeEventMethod != null) {
					System.err.println("Cannot use both currentChange() and currentChangeRT(Channel)."); 
				}
				else {
					RTEventRegister = true;
					currentChangeEventReportChannel = false;
				}
			}
		} catch (Exception e) {
			// function "currentChangeRT" not defined
		}

		// currentChangeRT(Channel)
		try {
			currentChangeEventRTMethod =  PAppletParent.getClass().getMethod("currentChangeRT", new Class<?>[] { Channel.class });
			if (currentChangeEventRTMethod != null) {
				if (currentChangeEventMethod != null) {
					System.err.println("Cannot use both currentChange() and currentChangeRT()."); 
				}
				else {
					RTEventRegister = true;
					currentChangeEventReportChannel = true;
				}
			}
		} catch (Exception e) {
			// function "currentChangeRT(Channel)" not defined
		}
	}

	@Override
	public void draw() {
		if (currentChangeFlag) {
			currentChangeFlag = false;
			try {
				if (currentChangeEventMethod != null) {
					if (currentChangeEventReportChannel) {
						currentChangeEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						currentChangeEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling currentChange() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				currentChangeEventMethod = null;
			}
		}
	}

	@Override
	public void pre() {
		if (RTEventRegister) {
			RTEventRegister = false;
			try {
				if (currentChangeEventReportChannel) { // currentChangeRT(Channel)
					((CurrentInput)device).addCurrentChangeListener(new CurrentInputCurrentChangeListener() {
						public void onCurrentChange(CurrentInputCurrentChangeEvent e) {
							//System.out.println(e.toString());
							try {
								if (currentChangeEventRTMethod != null) {
									currentChangeEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
								}
							} catch (Exception ex) {
								System.err.println("Disabling currentChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								currentChangeEventRTMethod = null;
							}
						}
					});
				}
				else { // currentChangeRT()
					((CurrentInput)device).addCurrentChangeListener(new CurrentInputCurrentChangeListener() {
						public void onCurrentChange(CurrentInputCurrentChangeEvent e) {
							//System.out.println(e.toString());
							try {
								if (currentChangeEventRTMethod != null) {
									currentChangeEventRTMethod.invoke(PAppletParent);
								}
							} catch (Exception ex) {
								System.err.println("Disabling currentChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								currentChangeEventRTMethod = null;
							}
						}
					});
				}
			} catch (Exception ex) {
		    	System.err.println("Disabling currentChangeRT() for " + deviceType + " because of an error:");
		    	ex.printStackTrace();
		    	currentChangeEventRTMethod = null;
		    }
		}
	}

	@Override
	public int read() {
		try {
			return (int)(((CurrentInput)device).getCurrent());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get current value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0; 
	}

	@Override
	public int getDataInterval() {
		try {
			return ((CurrentInput)device).getDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setDataInterval(int dataInterval) {
		try {
			((CurrentInput)device).setDataInterval(dataInterval);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set data interval value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public int getMinDataInterval() {
		try {
			return ((CurrentInput)device).getMinDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public int getMaxDataInterval() {
		try {
			return ((CurrentInput)device).getMaxDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public int getPowerSupply() {  // in V; 12, 24 or 0 for OFF
		if (deviceType == "DAQ1400") {
			try {
				PowerSupply ps = (((CurrentInput)device).getPowerSupply());
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

	@Override
	public void setPowerSupply(int ps) {
		if (deviceType == "DAQ1400") {
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
				((CurrentInput)device).setPowerSupply(p);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set power supply to device " + deviceType);
			}
		}
		else {
			System.err.println("setPowerSupply(int) is not valid for device of type " + deviceType);				
		}
	}

	@Override
	public float getCurrent() {
		try {
			return (float)(((CurrentInput)device).getCurrent());
		}
		catch (PhidgetException ex) {
			if 	(ex.getErrorCode() == com.phidget22.ErrorCode.UNKNOWN_VALUE) {
				System.err.println("Sensor value out of range");
			}
			else {
				System.err.println("Cannot get current value for device " + deviceType + " because of error: " + ex);
				PAppletParent.exit();
			}
		}
		return 0.0f;
	}

	@Override
	public float getMinCurrent() {
		try {
			return (float)(((CurrentInput)device).getMinCurrent());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get minimum current value for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public float getMaxCurrent() {
		try {
			return (float)(((CurrentInput)device).getMinCurrent());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get minimum current value for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public boolean getCurrentValidity() {
		try {
			@SuppressWarnings("unused")
			double reading = ((CurrentInput)device).getCurrent();
			return true;
		}
		catch (PhidgetException ex) {
			if 	(ex.getErrorCode() == com.phidget22.ErrorCode.UNKNOWN_VALUE) {
				return false;
			}
			else {
				System.err.println("Cannot check sensor value validity for device " + deviceType + " because of error: " + ex);
				PAppletParent.exit();
			}
		}
		return false;
	}

	@Override
	public float getCurrentChangeTrigger() {
		try {
			return (float)(((CurrentInput)device).getCurrentChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get current change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public void setCurrentChangeTrigger(float changeTrigger) {
		try {
			((CurrentInput)device).setCurrentChangeTrigger((double)changeTrigger);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set current change trigger for device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getMinCurrentChangeTrigger() {
		try {
			return (float)(((CurrentInput)device).getMinCurrentChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get minimum current change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public float getMaxCurrentChangeTrigger() {
		try {
			return (float)(((CurrentInput)device).getMaxCurrentChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get maximum current change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}
}
