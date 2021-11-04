package shenkar.SimplePhidgets;

import processing.core.*;
import java.lang.reflect.Method;
import com.phidget22.*;

public class P_MotorPositionController extends Device {
	// events
	Method dutyCycleChangeEventMethod;  // dutyCycleChange
	Method positionChangeEventMethod;  // positionChange
	boolean dutyCycleChangeFlag = false;
	boolean positionChangeFlag = false;
	boolean dutyCycleChangeEventReportChannel = false;
	boolean positionChangeEventReportChannel = false;

	// real-time events
	Method dutyCycleChangeEventRTMethod;  // dutyCycleChangeRT
	Method positionChangeEventRTMethod;  // positionChangeRT
	boolean RTDutyCycleChangeEventRegister = false;
	boolean RTPositionChangeEventRegister = false;

	public P_MotorPositionController(PApplet P5Parent, Channel ChParent, String type, int serialNum, int portNum, int chNum) {
		super(P5Parent, ChParent, type, serialNum, portNum, chNum);

		// pre-opening setup
		try {
			device = new MotorPositionController();
		}	catch (PhidgetException ex) {
			System.err.println("Could not open device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
			PAppletParent.exit();
		}

		// device opening - all VINT devices
		/*
		switch (deviceType) {
		case "DCC1000":  // DC Motor Phidget			[no stallVelocity]
		case "DCC1001":  // 2A DC Motor Phidget			[no currentRegulatorGain, fanMode, IOMode, stallVelocity]
		case "DCC1002":  // 4A DC Motor Phidget			[no currentRegulatorGain, fanMode, IOMode, stallVelocity]
		case "DCC1100":  // Brushless DC Motor Phidget	[no currentLimit, currentRegulatorGain, fanMode, IOMode]
		*/
		init(false);

		// post-opening setup
		try {
			// set maximum data rate as default
			((MotorPositionController)device).setDataInterval(((MotorPositionController)device).getMinDataInterval());
		}
		catch (PhidgetException ex) {
			System.err.println("Could not set data interval for device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
		}
		attachListeners();
	}

	// check if "dutyCycleChange()" or "positionChange()" were defined in the sketch and create a listener for it.
	void attachListeners() {
		// positionChange()
		try {
			positionChangeEventMethod =  PAppletParent.getClass().getMethod("positionChange");
			if (positionChangeEventMethod != null) {
				positionChangeEventReportChannel = false;
				((MotorPositionController)device).addPositionChangeListener(new MotorPositionControllerPositionChangeListener() {
					public void onPositionChange(MotorPositionControllerPositionChangeEvent e) {
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
				((MotorPositionController)device).addPositionChangeListener(new MotorPositionControllerPositionChangeListener() {
					public void onPositionChange(MotorPositionControllerPositionChangeEvent e) {
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
					RTPositionChangeEventRegister = true;
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
					RTPositionChangeEventRegister = true;
					positionChangeEventReportChannel = true;
				}
			}
		} catch (Exception e) {
			// function "positionChangeRT(Channel)" not defined
		}
		
		// dutyCycleChange()
		try {
			dutyCycleChangeEventMethod =  PAppletParent.getClass().getMethod("dutyCycleChange");
			if (dutyCycleChangeEventMethod != null) {
				if (deviceType.equals("1065")) {
					dutyCycleChangeEventReportChannel = false;
					((MotorPositionController)device).addDutyCycleUpdateListener(new MotorPositionControllerDutyCycleUpdateListener() {
						public void onDutyCycleUpdate(MotorPositionControllerDutyCycleUpdateEvent e) {
							//System.out.println(e.toString());
							dutyCycleChangeFlag = true;
						}
					});
				}
				else {
					System.err.println("Device " + deviceType + " doesn't support the dutyCycleChange() event function"); 
				}
			}
		} catch (Exception e) {
			// function "dutyCycleChange" not defined
		}

		// dutyCycleChange(Channel)
		try {
			dutyCycleChangeEventMethod =  PAppletParent.getClass().getMethod("dutyCycleChange", new Class<?>[] { Channel.class });
			if (dutyCycleChangeEventMethod != null) {
				if (deviceType.equals("1065")) {
					dutyCycleChangeEventReportChannel = true;
					((MotorPositionController)device).addDutyCycleUpdateListener(new MotorPositionControllerDutyCycleUpdateListener() {
						public void onDutyCycleUpdate(MotorPositionControllerDutyCycleUpdateEvent e) {
							//System.out.println(e.toString());
							dutyCycleChangeFlag = true;
						}
					});
				}
				else {
					System.err.println("Device " + deviceType + " doesn't support the dutyCycleChange() event function"); 
				}
			}
		} catch (Exception e) {
			// function "dutyCycleChange(Channel)" not defined
		}

		// dutyCycleChangeRT()
		try {
			dutyCycleChangeEventRTMethod =  PAppletParent.getClass().getMethod("dutyCycleChangeRT");
			if (dutyCycleChangeEventRTMethod != null) {
				if (deviceType.equals("1065")) {
					if (dutyCycleChangeEventMethod != null) {
						System.err.println("Cannot use both dutyCycleChange() and dutyCycleChangeRT()."); 
					}
					else {
						RTDutyCycleChangeEventRegister = true;
						dutyCycleChangeEventReportChannel = false;
					}
				}
				else {
					System.err.println("Device " + deviceType + " doesn't support the dutyCycleChangeRT() event function"); 
				}
			}
		} catch (Exception e) {
			// function "dutyCycleChangeRT()" not defined
		}

		// dutyCycleChangeRT(Channel)
		try {
			dutyCycleChangeEventRTMethod =  PAppletParent.getClass().getMethod("dutyCycleChangeRT", new Class<?>[] { Channel.class });
			if (dutyCycleChangeEventRTMethod != null) {
				if (deviceType.equals("1065")) {
					if (dutyCycleChangeEventMethod != null) {
						System.err.println("Cannot use both dutyCycleChange() and dutyCycleChangeRT()."); 
					}
					else {
						RTDutyCycleChangeEventRegister = true;
						dutyCycleChangeEventReportChannel = true;
					}
				}
				else {
					System.err.println("Device " + deviceType + " doesn't support the dutyCycleChangeRT() event function"); 
				}
			}
		} catch (Exception e) {
			// function "dutyCycleChangeRT()" not defined
		}
	}

	@Override
	public void pre() {
		// dutyCycleChangeRT
		if (RTDutyCycleChangeEventRegister) {
			RTDutyCycleChangeEventRegister = false;
			try {
				if (dutyCycleChangeEventReportChannel) { // dutyCycleChangeRT(Channel)
					((MotorPositionController)device).addDutyCycleUpdateListener(new MotorPositionControllerDutyCycleUpdateListener() {
						public void onDutyCycleUpdate(MotorPositionControllerDutyCycleUpdateEvent e) {
							//System.out.println(e.toString());
							try {
								if (dutyCycleChangeEventRTMethod != null) {
									dutyCycleChangeEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
								}
							} catch (Exception ex) {
								System.err.println("Disabling dutyCycleChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								dutyCycleChangeEventRTMethod = null;
							}
						}
					});
				}
				else { // dutyCycleChangeRT()
					((MotorPositionController)device).addDutyCycleUpdateListener(new MotorPositionControllerDutyCycleUpdateListener() {
						public void onDutyCycleUpdate(MotorPositionControllerDutyCycleUpdateEvent e) {
							//System.out.println(e.toString());
							try {
								if (dutyCycleChangeEventRTMethod != null) {
									dutyCycleChangeEventRTMethod.invoke(PAppletParent);
								}
							} catch (Exception ex) {
								System.err.println("Disabling dutyCycleChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								dutyCycleChangeEventRTMethod = null;
							}
						}
					});
				}
			} catch (Exception ex) {
		    	System.err.println("Disabling dutyCycleChangeRT() for " + deviceType + " because of an error:");
		    	ex.printStackTrace();
		    	dutyCycleChangeEventRTMethod = null;
		    }
		}

		// positionChangeRT
		if (RTPositionChangeEventRegister) {
			RTPositionChangeEventRegister = false;
			try {
				if (positionChangeEventReportChannel) { // positionChangeRT(Channel)
					((MotorPositionController)device).addPositionChangeListener(new MotorPositionControllerPositionChangeListener() {
						public void onPositionChange(MotorPositionControllerPositionChangeEvent e) {
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
					((MotorPositionController)device).addPositionChangeListener(new MotorPositionControllerPositionChangeListener() {
						public void onPositionChange(MotorPositionControllerPositionChangeEvent e) {
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
		if (dutyCycleChangeFlag) {
			dutyCycleChangeFlag = false;
			try {
				if (dutyCycleChangeEventMethod != null) {
					if (dutyCycleChangeEventReportChannel) {
						dutyCycleChangeEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						dutyCycleChangeEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling dutyCycleChange() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				dutyCycleChangeEventMethod = null;
			}
		}

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
	 * stop the motor before closing
	 * 
	 */
	@Override
	public void close() {
		if (device != null) {
			try {
				((MotorPositionController)device).setEngaged(false);					
				String deviceClass = device.getChannelClassName();
				device.close();
				System.out.println(deviceType + " of type " + deviceClass + " on port " + hubPort + " Closed");
			}
			catch (PhidgetException ex) {
				System.err.println("Could not close device " + deviceType + " because of error:" + ex.toString());
				PAppletParent.exit();
			}
		}
	}

	@Override
	public float getAcceleration() {
		try {
			return (float)(((MotorPositionController)device).getAcceleration());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get acceleration value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setAcceleration(float accel) {
		try {
			((MotorPositionController)device).setAcceleration((double)accel);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set acceleration value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getMinAcceleration() {
		try {
			return (float)(((MotorPositionController)device).getMinAcceleration());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min acceleration value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public float getMaxAcceleration() {
		try {
			return (float)(((MotorPositionController)device).getMaxAcceleration());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max acceleration value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public float getCurrentLimit() {
		if (!deviceType.equals("DCC1100")) {
			try {
				return (float)(((MotorPositionController)device).getCurrentLimit());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get current limit for device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getCurrentLimit() is not valid for device of type " + deviceType);	
		}
		return 0;
	}

	@Override
	public void setCurrentLimit(float curr) {
		if (!deviceType.equals("DCC1100")) {
			try {
				((MotorPositionController)device).setCurrentLimit((double)curr);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set current limit to device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getCurrentLimit() is not valid for device of type " + deviceType);	
		}
	}

	@Override
	public float getMinCurrentLimit() {
		if (!deviceType.equals("DCC1100")) {
			try {
				return (float)(((MotorPositionController)device).getMinCurrentLimit());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get min current limit for device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getCurrentLimit() is not valid for device of type " + deviceType);	
		}
		return 0;
	}

	@Override
	public float getMaxCurrentLimit() {
		if (!deviceType.equals("DCC1100")) {
			try {
				return (float)(((MotorPositionController)device).getMaxCurrentLimit());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get max current limit for device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getCurrentLimit() is not valid for device of type " + deviceType);	
		}
		return 0;
	}
	
	@Override
	public float getCurrentRegulatorGain() {
		if (deviceType.equals("DCC1000")) {
			try {
				return (float)(((MotorPositionController)device).getCurrentRegulatorGain());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get current regulator gain value from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getCurrentRegulatorGain() is not valid for device of type " + deviceType);	
		}
		return 0;
	}
	
	@Override
	public void setCurrentRegulatorGain(float gain) {
		if (deviceType.equals("DCC1000")) {
			try {
				((MotorPositionController)device).setCurrentRegulatorGain((double)gain);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set current regulator gain value to device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("setCurrentRegulatorGain() is not valid for device of type " + deviceType);	
		}
	}

	@Override
	public float getMinCurrentRegulatorGain() {
		if (deviceType.equals("DCC1000")) {
			try {
				return (float)(((MotorPositionController)device).getMinCurrentRegulatorGain());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get minimum current regulator gain value from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getMinCurrentRegulatorGain() is not valid for device of type " + deviceType);	
		}
		return 0;
	}
	
	@Override
	public float getMaxCurrentRegulatorGain() {
		if (deviceType.equals("DCC1000")) {
			try {
				return (float)(((MotorPositionController)device).getMaxCurrentRegulatorGain());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get maximum current regulator gain value from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getMaxCurrentRegulatorGain() is not valid for device of type " + deviceType);	
		}
		return 0;
	}

	@Override
	public int getDataInterval() {
		try {
			return ((MotorPositionController)device).getDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setDataInterval(int dataInterval) {
		try {
			((MotorPositionController)device).setDataInterval(dataInterval);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set data interval value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public int getMinDataInterval() {
		try {
			return ((MotorPositionController)device).getMinDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public int getMaxDataInterval() {
		try {
			return ((MotorPositionController)device).getMaxDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setDeadBand(float accel) {
		try {
			((MotorPositionController)device).setDeadBand((double)accel);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set dead-band value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getDeadBand() {
		try {
			return (float)(((MotorPositionController)device).getDeadBand());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get dead-band value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public float getDutyCycle() {
		try {
			return (float)(((MotorPositionController)device).getDutyCycle());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get duty cycle value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public boolean getEngaged() {
		try {
			return ((MotorPositionController)device).getEngaged();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get engaged state from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return false;
	}

	@Override
	public void setEngaged(boolean eng) {
		try {
			((MotorPositionController)device).setEngaged(eng);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set engaged state to device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
	}

	/*
	 * enables the failsafe feature for the channel, with a given failsafe time
	 * 
	 * @param failsafeTime time to enter fail safe mode (milliseconds)
	 */
	@Override
	public void enableFailsafe(int failsafeTime) {
		try {
			((MotorPositionController)device).enableFailsafe(failsafeTime);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set failsafe for device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
	}	

	/*
	 * The minimum value that failsafe time can be set to.
	 * 
	 * @return int
	 */
	@Override
	public int getMinFailsafeTime() {
		try {
			return ((MotorPositionController)device).getMinFailsafeTime();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min falesafe time from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	/*
	 * The maximum value that failsafe time can be set to.
	 * 
	 * @return int
	 */
	@Override
	public int getMaxFailsafeTime() {
		try {
			return ((MotorPositionController)device).getMaxFailsafeTime();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max falesafe time from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	/*
	 * resets the failsafe timer
	 * 
	 */
	@Override
	public void resetFailsafe() {
		try {
			((MotorPositionController)device).resetFailsafe();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot reset failsafe timer for device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
	}	
	
	@Override
	public String getFanMode() {
		if (deviceType.equals("DCC1000")) {
			try {
				switch (((MotorPositionController)device).getFanMode()) {
				case OFF:
					return "Off";
	
				case ON:
					return "On";

				case AUTO:
					return "Auto";
				}
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get control mode from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getFanMode() is not valid for device of type " + deviceType);	
		}
		return "";
	}
	
	@Override
	public void setFanMode(String mode) {
		if (deviceType.equals("DCC1000")) {
			try {
				switch (mode.toUpperCase()) {
				case "ON":
					((MotorPositionController)device).setFanMode(FanMode.ON);
					break;
	
				case "OFF":
					((MotorPositionController)device).setFanMode(FanMode.OFF);
					break;
	
				case "AUTO":
					((MotorPositionController)device).setFanMode(FanMode.AUTO);
					break;
	
				default:
					System.err.println("Invalid fan mode: " + mode + ". Use only \"On\", \"Off\" or \"Auto\"");			
				}
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set fan mode to device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getFanMode() is not valid for device of type " + deviceType);	
		}
	}
	
	@Override
	public String getIOMode() { 
		if (deviceType.equals("DCC1000")) {
			try {
				EncoderIOMode em = (((MotorPositionController)device).getIOMode());
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
		if (deviceType.equals("DCC1000")) {
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
				((MotorPositionController)device).setIOMode(i);
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
	public float getKd() {
		try {
			return (float)(((MotorPositionController)device).getKd());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get Kd value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setKd(float kd) {
		try {
			((MotorPositionController)device).setKd((double)kd);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set Kd value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getKi() {
		try {
			return (float)(((MotorPositionController)device).getKi());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get Ki value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setKi(float ki) {
		try {
			((MotorPositionController)device).setKi((double)ki);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set Ki value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getKp() {
		try {
			return (float)(((MotorPositionController)device).getKp());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get Kp value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setKp(float kp) {
		try {
			((MotorPositionController)device).setKp((double)kp);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set Kp value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getPosition() {
		try {
			return (float)(((MotorPositionController)device).getPosition());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get position value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0;
	}

	@Override
	public float getMinPosition() {
		try {
			return (float)(((MotorPositionController)device).getMinPosition());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min position value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public float getMaxPosition() {
		try {
			return (float)(((MotorPositionController)device).getMaxPosition());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max position value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void addPositionOffset(float offset) {
		try {
			((MotorPositionController)device).addPositionOffset((double)offset);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot add position offset value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public void addPositionOffset(int offset) {
		try {
			((MotorPositionController)device).addPositionOffset((double)offset);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot add position offset value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getRescaleFactor() {
		try {
			return (float)(((MotorPositionController)device).getRescaleFactor());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get rescale factor for device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setRescaleFactor(float fctr) {
		try {
			((MotorPositionController)device).setRescaleFactor((double)fctr);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set rescale factor to device " + deviceType + " because of error: " + ex);
		}
	}

	
	@Override
	public float getStallVelocity() {
		if (deviceType.equals("DCC1100")) {
			try {
				return (float)(((MotorPositionController)device).getStallVelocity());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get stall velocity for device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getStallVelocity() is not valid for device of type " + deviceType);				
		}
		return 0;
	}

	@Override
	public void setStallVelocity(float vel) {
		if (deviceType.equals("DCC1100")) {
			try {
				((MotorPositionController)device).setStallVelocity((double)vel);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set stall velocity to device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("setStallVelocity(float) is not valid for device of type " + deviceType);				
		}
	}
	
	@Override
	public float getMinStallVelocity() {
		if (deviceType.equals("DCC1100")) {
			try {
				return (float)(((MotorPositionController)device).getMinStallVelocity());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get minimum stall velocity for device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getMinStallVelocity() is not valid for device of type " + deviceType);				
		}
		return 0;
	}

	@Override
	public float getMaxStallVelocity() {
		if (deviceType.equals("DCC1100")) {
			try {
				return (float)(((MotorPositionController)device).getMaxStallVelocity());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get maximum stall velocity for device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getMaxStallVelocity() is not valid for device of type " + deviceType);				
		}
		return 0;
	}

	@Override
	public void setTargetPosition(float pos) {
		try {
			((MotorPositionController)device).setTargetPosition((double)pos);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set target poition to device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
	}

	@Override
	public float getTargetPosition() {
		try {
			return (float)(((MotorPositionController)device).getTargetPosition());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get target position from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0;
	}
	
	@Override
	public float getVelocityLimit() {
		try {
			return (float)(((MotorPositionController)device).getVelocityLimit());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get velocity limit value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}
	
	@Override
	public void setVelocityLimit(float vel) {
		try {
			((MotorPositionController)device).setVelocityLimit((double)vel);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set velocity limit value to device " + deviceType + " because of error: " + ex);
		}
	}
	
	@Override
	public float getMinVelocityLimit() {
		try {
			return (float)(((MotorPositionController)device).getMinVelocityLimit());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min velocity limit value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}
	
	@Override
	public float getMaxVelocityLimit() {
		try {
			return (float)(((MotorPositionController)device).getMaxVelocityLimit());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max velocity limit value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}
}
