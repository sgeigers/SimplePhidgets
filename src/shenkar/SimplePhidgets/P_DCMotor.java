package shenkar.SimplePhidgets;

import processing.core.*;
import java.lang.reflect.Method;
import com.phidget22.*;

public class P_DCMotor extends Device {
	// events
	Method backEMFChangeEventMethod;  // backEMFChange
	Method velocityChangeEventMethod;  // velocityChange
	Method brakingStrengthChangeEventMethod;   // brakingStrengthChange
	boolean backEMFChangeFlag = false;
	boolean velocityChangeFlag = false;
	boolean brakingStrengthChangeFlag = false;
	boolean backEMFChangeEventReportChannel = false;
	boolean velocityChangeEventReportChannel = false;
	boolean brakingStrengthChangeEventReportChannel = false;

	// real-time events
	Method backEMFChangeEventRTMethod;  // backEMFChangeRT
	Method velocityChangeEventRTMethod;  // velocityChangeRT
	Method brakingStrengthChangeEventRTMethod;   // brakingStrengthChangeRT
	boolean RTbackEMFChangeEventRegister = false;
	boolean RTVelocityChangeEventRegister = false;
	boolean RTbrakingStrengthChangeEventRegister = false;

	public P_DCMotor(PApplet P5Parent, Channel ChParent, String type, int serialNum, int portNum, int chNum) {
		super(P5Parent, ChParent, type, serialNum, portNum, chNum);

		// pre-opening setup
		try {
			device = new DCMotor();
		}	catch (PhidgetException ex) {
			System.err.println("Could not open device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
			PAppletParent.exit();
		}

		// device opening
		switch (deviceType) {
		case "1060":  // PhidgetMotorControl LV
		case "1064":  // PhidgetMotorControl HC
		case "1065":  // PhidgetMotorControl 1-Motor
			initNoHub();
			break;

		case "DCC1000":  // DC Motor Phidget
		case "DCC1001":  // 2A DC Motor Phidget
		case "DCC1002":  // 4A DC Motor Phidget
		case "DCC1003":  // 2x DC Motor Phidget
			init(false);
			break;

		default:
			System.err.println("Could not open device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
			break;
		}

		// post-opening setup
		attachListeners();
	}

	// check if "backEMFChange()", "velocityChange()" or "brakingStrengthChange()" were defined in the sketch and create a listener for it.
	void attachListeners() {
		// velocityChange()
		try {
			velocityChangeEventMethod =  PAppletParent.getClass().getMethod("velocityChange");
			if (velocityChangeEventMethod != null) {
				velocityChangeEventReportChannel = false;
				((DCMotor)device).addVelocityUpdateListener(new DCMotorVelocityUpdateListener() {
					public void onVelocityUpdate(DCMotorVelocityUpdateEvent e) {
						//System.out.println(e.toString());
						velocityChangeFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "velocityChange" not defined
		}

		// velocityChange(Channel)
		try {
			velocityChangeEventMethod =  PAppletParent.getClass().getMethod("velocityChange", new Class<?>[] { Channel.class });
			if (velocityChangeEventMethod != null) {
				velocityChangeEventReportChannel = true;
				((DCMotor)device).addVelocityUpdateListener(new DCMotorVelocityUpdateListener() {
					public void onVelocityUpdate(DCMotorVelocityUpdateEvent e) {
						//System.out.println(e.toString());
						velocityChangeFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "velocityChange(Channel)" not defined
		}

		// velocityChangeRT()
		try {
			velocityChangeEventRTMethod =  PAppletParent.getClass().getMethod("velocityChangeRT");
			if (velocityChangeEventRTMethod != null) {
				if (velocityChangeEventMethod != null) {
					System.err.println("Cannot use both velocityChange() and velocityChangeRT()."); 
				}
				else {
					RTVelocityChangeEventRegister = true;
					velocityChangeEventReportChannel = false;
				}
			}
		} catch (Exception e) {
			// function "velocityChangeRT()" not defined
		}

		// velocityChangeRT(Channel)
		try {
			velocityChangeEventRTMethod =  PAppletParent.getClass().getMethod("velocityChangeRT", new Class<?>[] { Channel.class });
			if (velocityChangeEventRTMethod != null) {
				if (velocityChangeEventMethod != null) {
					System.err.println("Cannot use both velocityChange() and velocityChangeRT()."); 
				}
				else {
					RTVelocityChangeEventRegister = true;
					velocityChangeEventReportChannel = true;
				}
			}
		} catch (Exception e) {
			// function "velocityChangeRT(Channel)" not defined
		}
		
		// backEMFChange()
		try {
			backEMFChangeEventMethod =  PAppletParent.getClass().getMethod("backEMFChange");
			if (backEMFChangeEventMethod != null) {
				if (deviceType.equals("1065")) {
					backEMFChangeEventReportChannel = false;
					((DCMotor)device).addBackEMFChangeListener(new DCMotorBackEMFChangeListener() {
						public void onBackEMFChange(DCMotorBackEMFChangeEvent e) {
							//System.out.println(e.toString());
							backEMFChangeFlag = true;
						}
					});
				}
				else {
					System.err.println("Device " + deviceType + " doesn't support the backEMFChange() event function"); 
				}
			}
		} catch (Exception e) {
			// function "backEMFChange" not defined
		}

		// backEMFChange(Channel)
		try {
			backEMFChangeEventMethod =  PAppletParent.getClass().getMethod("backEMFChange", new Class<?>[] { Channel.class });
			if (backEMFChangeEventMethod != null) {
				if (deviceType.equals("1065")) {
					backEMFChangeEventReportChannel = true;
					((DCMotor)device).addBackEMFChangeListener(new DCMotorBackEMFChangeListener() {
						public void onBackEMFChange(DCMotorBackEMFChangeEvent e) {
							//System.out.println(e.toString());
							backEMFChangeFlag = true;
						}
					});
				}
				else {
					System.err.println("Device " + deviceType + " doesn't support the backEMFChange() event function"); 
				}
			}
		} catch (Exception e) {
			// function "backEMFChange(Channel)" not defined
		}

		// backEMFChangeRT()
		try {
			backEMFChangeEventRTMethod =  PAppletParent.getClass().getMethod("backEMFChangeRT");
			if (backEMFChangeEventRTMethod != null) {
				if (deviceType.equals("1065")) {
					if (backEMFChangeEventMethod != null) {
						System.err.println("Cannot use both backEMFChange() and backEMFChangeRT()."); 
					}
					else {
						RTbackEMFChangeEventRegister = true;
						backEMFChangeEventReportChannel = false;
					}
				}
				else {
					System.err.println("Device " + deviceType + " doesn't support the backEMFChangeRT() event function"); 
				}
			}
		} catch (Exception e) {
			// function "backEMFChangeRT()" not defined
		}

		// backEMFChangeRT(Channel)
		try {
			backEMFChangeEventRTMethod =  PAppletParent.getClass().getMethod("backEMFChangeRT", new Class<?>[] { Channel.class });
			if (backEMFChangeEventRTMethod != null) {
				if (deviceType.equals("1065")) {
					if (backEMFChangeEventMethod != null) {
						System.err.println("Cannot use both backEMFChange() and backEMFChangeRT()."); 
					}
					else {
						RTbackEMFChangeEventRegister = true;
						backEMFChangeEventReportChannel = true;
					}
				}
				else {
					System.err.println("Device " + deviceType + " doesn't support the backEMFChangeRT() event function"); 
				}
			}
		} catch (Exception e) {
			// function "backEMFChangeRT()" not defined
		}

		// brakingStrengthChange()
		try {
			brakingStrengthChangeEventMethod =  PAppletParent.getClass().getMethod("brakingStrengthChange");
			if (brakingStrengthChangeEventMethod != null) {
				switch (deviceType) {
				case "DCC1000":  // DC Motor Phidget
				case "DCC1001":  // 2A DC Motor Phidget
				case "DCC1002":  // 4A DC Motor Phidget
				case "DCC1003":  // 2x DC Motor Phidget
					brakingStrengthChangeEventReportChannel = false;
					((DCMotor)device).addBrakingStrengthChangeListener(new DCMotorBrakingStrengthChangeListener() {
						public void onBrakingStrengthChange(DCMotorBrakingStrengthChangeEvent e) {
							//System.out.println(e.toString());
							brakingStrengthChangeFlag = true;
						}
					});
					break;

				default:
					System.err.println("Device " + deviceType + " doesn't support the brakingStrengthChange() event function"); 
					break;
				}
			}
		} catch (Exception e) {
			// function "brakingStrengthChange" not defined
		}

		// brakingStrengthChange(Channel)
		try {
			brakingStrengthChangeEventMethod =  PAppletParent.getClass().getMethod("brakingStrengthChange", new Class<?>[] { Channel.class });
			if (brakingStrengthChangeEventMethod != null) {
				switch (deviceType) {
				case "DCC1000":  // DC Motor Phidget
				case "DCC1001":  // 2A DC Motor Phidget
				case "DCC1002":  // 4A DC Motor Phidget
				case "DCC1003":  // 2x DC Motor Phidget
					brakingStrengthChangeEventReportChannel = true;
					((DCMotor)device).addBrakingStrengthChangeListener(new DCMotorBrakingStrengthChangeListener() {
						public void onBrakingStrengthChange(DCMotorBrakingStrengthChangeEvent e) {
							//System.out.println(e.toString());
							brakingStrengthChangeFlag = true;
						}
					});
					break;

				default:
					System.err.println("Device " + deviceType + " doesn't support the brakingStrengthChange() event function"); 
					break;
				}
			}
		} catch (Exception e) {
			// function "brakingStrengthChange(Channel)" not defined
		}

		//  brakingStrengthChangeRT()
		try {
			 brakingStrengthChangeEventRTMethod =  PAppletParent.getClass().getMethod("brakingStrengthChangeRT");
			if (brakingStrengthChangeEventRTMethod != null) {
				switch (deviceType) {
				case "DCC1000":  // DC Motor Phidget
				case "DCC1001":  // 2A DC Motor Phidget
				case "DCC1002":  // 4A DC Motor Phidget
				case "DCC1003":  // 2x DC Motor Phidget
					if (velocityChangeEventMethod != null) {
						System.err.println("Cannot use both velocityChange() and velocityChangeRT()."); 
					}
					else {
						RTbrakingStrengthChangeEventRegister = true;
						brakingStrengthChangeEventReportChannel = false;
					}
					break;

				default:
					System.err.println("Device " + deviceType + " doesn't support the brakingStrengthChangeRT() event function"); 
					break;
				}
			}
		} catch (Exception e) {
			// function "brakingStrengthChangeRT()" not defined
		}

		//  brakingStrengthChangeRT(Channel)
		try {
			 brakingStrengthChangeEventRTMethod =  PAppletParent.getClass().getMethod("brakingStrengthChangeRT", new Class<?>[] { Channel.class });
			if (brakingStrengthChangeEventRTMethod != null) {
				switch (deviceType) {
				case "DCC1000":  // DC Motor Phidget
				case "DCC1001":  // 2A DC Motor Phidget
				case "DCC1002":  // 4A DC Motor Phidget
				case "DCC1003":  // 2x DC Motor Phidget
					if (velocityChangeEventMethod != null) {
						System.err.println("Cannot use both velocityChange() and velocityChangeRT()."); 
					}
					else {
						RTbrakingStrengthChangeEventRegister = true;
						brakingStrengthChangeEventReportChannel = true;
					}
					break;

				default:
					System.err.println("Device " + deviceType + " doesn't support the brakingStrengthChangeRT() event function"); 
					break;
				}
			}
		} catch (Exception e) {
			// function "brakingStrengthChangeRT(Channel)" not defined
		}
	}

	@Override
	public void pre() {
		// backEMFChangeRT
		if (RTbackEMFChangeEventRegister) {
			RTbackEMFChangeEventRegister = false;
			try {
				if (backEMFChangeEventReportChannel) { // backEMFChangeRT(Channel)
					((DCMotor)device).addBackEMFChangeListener(new DCMotorBackEMFChangeListener() {
						public void onBackEMFChange(DCMotorBackEMFChangeEvent e) {
							//System.out.println(e.toString());
							try {
								if (backEMFChangeEventRTMethod != null) {
									backEMFChangeEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
								}
							} catch (Exception ex) {
								System.err.println("Disabling backEMFChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								backEMFChangeEventRTMethod = null;
							}
						}
					});
				}
				else { // backEMFChangeRT()
					((DCMotor)device).addBackEMFChangeListener(new DCMotorBackEMFChangeListener() {
						public void onBackEMFChange(DCMotorBackEMFChangeEvent e) {
							//System.out.println(e.toString());
							try {
								if (backEMFChangeEventRTMethod != null) {
									backEMFChangeEventRTMethod.invoke(PAppletParent);
								}
							} catch (Exception ex) {
								System.err.println("Disabling backEMFChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								backEMFChangeEventRTMethod = null;
							}
						}
					});
				}
			} catch (Exception ex) {
		    	System.err.println("Disabling backEMFChangeRT() for " + deviceType + " because of an error:");
		    	ex.printStackTrace();
		    	backEMFChangeEventRTMethod = null;
		    }
		}

		// velocityChangeRT
		if (RTVelocityChangeEventRegister) {
			RTVelocityChangeEventRegister = false;
			try {
				if (velocityChangeEventReportChannel) { // velocityChangeRT(Channel)
					((DCMotor)device).addVelocityUpdateListener(new DCMotorVelocityUpdateListener() {
						public void onVelocityUpdate(DCMotorVelocityUpdateEvent e) {
							//System.out.println(e.toString());
							try {
								if (velocityChangeEventRTMethod != null) {
									velocityChangeEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
								}
							} catch (Exception ex) {
								System.err.println("Disabling velocityChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								velocityChangeEventRTMethod = null;
							}
						}
					});
				}
				else { // velocityChangeRT()
					((DCMotor)device).addVelocityUpdateListener(new DCMotorVelocityUpdateListener() {
						public void onVelocityUpdate(DCMotorVelocityUpdateEvent e) {
							//System.out.println(e.toString());
							try {
								if (velocityChangeEventRTMethod != null) {
									velocityChangeEventRTMethod.invoke(PAppletParent);
								}
							} catch (Exception ex) {
								System.err.println("Disabling velocityChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								velocityChangeEventRTMethod = null;
							}
						}
					});
				}
			} catch (Exception ex) {
		    	System.err.println("Disabling velocityChangeRT() for " + deviceType + " because of an error:");
		    	ex.printStackTrace();
		    	velocityChangeEventRTMethod = null;
		    }
		}

		// brakingStrengthChangeRT
		if (RTbrakingStrengthChangeEventRegister) {
			RTbrakingStrengthChangeEventRegister = false;
			try {
				if (brakingStrengthChangeEventReportChannel) { // brakingStrengthChangeRT(Channel)
					((DCMotor)device).addBrakingStrengthChangeListener(new DCMotorBrakingStrengthChangeListener() {
						public void onBrakingStrengthChange(DCMotorBrakingStrengthChangeEvent e) {
							//System.out.println(e.toString());
							try {
								if (brakingStrengthChangeEventRTMethod != null) {
									brakingStrengthChangeEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
								}
							} catch (Exception ex) {
								System.err.println("Disabling brakingStrengthChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								brakingStrengthChangeEventRTMethod = null;
							}
						}
					});
				}
				else { // brakingStrengthChangeRT()
					((DCMotor)device).addBrakingStrengthChangeListener(new DCMotorBrakingStrengthChangeListener() {
						public void onBrakingStrengthChange(DCMotorBrakingStrengthChangeEvent e) {
							//System.out.println(e.toString());
							try {
								if (brakingStrengthChangeEventRTMethod != null) {
									brakingStrengthChangeEventRTMethod.invoke(PAppletParent);
								}
							} catch (Exception ex) {
								System.err.println("Disabling brakingStrengthChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								brakingStrengthChangeEventRTMethod = null;
							}
						}
					});
				}
			} catch (Exception ex) {
		    	System.err.println("Disabling brakingStrengthChangeRT() for " + deviceType + " because of an error:");
		    	ex.printStackTrace();
		    	brakingStrengthChangeEventRTMethod = null;
		    }
		}
	}
	
	/**
	 * handles events. Do not call.
	 * 
	 */	
	@Override
	public void draw() {
		if (backEMFChangeFlag) {
			backEMFChangeFlag = false;
			try {
				if (backEMFChangeEventMethod != null) {
					if (backEMFChangeEventReportChannel) {
						backEMFChangeEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						backEMFChangeEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling backEMFChange() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				backEMFChangeEventMethod = null;
			}
		}

		if (velocityChangeFlag) {
			velocityChangeFlag = false;
			try {
				if (velocityChangeEventMethod != null) {
					if (velocityChangeEventReportChannel) {
						velocityChangeEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						velocityChangeEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling velocityChange() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				velocityChangeEventMethod = null;
			}
		}

		if (brakingStrengthChangeFlag) {
			brakingStrengthChangeFlag = false;
			try {
				if (brakingStrengthChangeEventMethod != null) {
					if (brakingStrengthChangeEventReportChannel) {
						brakingStrengthChangeEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						brakingStrengthChangeEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling brakingStrengthChange() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				brakingStrengthChangeEventMethod = null;
			}
		}
}

	/*
	 * stop all motors before closing
	 * 
	 */
	@Override
	public void close() {
		if (device != null) {
			try {
				((DCMotor)device).setTargetVelocity(0);	
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

	/*
	 * most basic way to use the channel. Runs the motor in desired PWM value (-1 to 1)
	 * 
	 */
	@Override
	public void setTargetVelocity(float vel) {
		try {
			((DCMotor)device).setTargetVelocity((double)vel);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set target velocity to device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
	}

	@Override
	public float getTargetVelocity() {
		try {
			return (float)(((DCMotor)device).getTargetVelocity());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get target velocity from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0;
	}

	@Override
	public float getAcceleration() {
		try {
			return (float)(((DCMotor)device).getAcceleration());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get acceleration value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setAcceleration(float accel) {
		try {
			((DCMotor)device).setAcceleration((double)accel);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set acceleration value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getMinAcceleration() {
		try {
			return (float)(((DCMotor)device).getMinAcceleration());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min acceleration value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public float getMaxAcceleration() {
		try {
			return (float)(((DCMotor)device).getMaxAcceleration());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max acceleration value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public float getBackEMF() {
		if (deviceType.equals("1065")) {
			try {
				return (float)(((DCMotor)device).getBackEMF());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get back-EMF value from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getBackEMF() is not valid for device of type " + deviceType);	
		}
		return 0;
	}

	@Override
	public boolean getBackEMFSensingState() {
		if (deviceType.equals("1065")) {
			try {
				return ((DCMotor)device).getBackEMFSensingState();
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get back-EMF sensing state from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getBackEMF() is not valid for device of type " + deviceType);	
		}
		return false;
	}

	@Override
	public void setBackEMFSensingState(boolean state) {
		if (deviceType.equals("1065")) {
			try {
				((DCMotor)device).setBackEMFSensingState(state);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set back-EMF sensing state to device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getBackEMF() is not valid for device of type " + deviceType);	
		}
	}

	@Override
	public float getBrakingStrength() {
		switch (deviceType) {
		case "DCC1000":  // DC Motor Phidget
		case "DCC1001":  // 2A DC Motor Phidget
		case "DCC1002":  // 4A DC Motor Phidget
		case "DCC1003":  // 2x DC Motor Phidget
			try {
				return (float)(((DCMotor)device).getBrakingStrength());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get breaking strength for device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("getBrakingStrength() is not valid for device of type " + deviceType);	
			break;
		}
		return 0;
	}
	
	@Override
	public float getMinBrakingStrength() {
		switch (deviceType) {
		case "DCC1000":  // DC Motor Phidget
		case "DCC1001":  // 2A DC Motor Phidget
		case "DCC1002":  // 4A DC Motor Phidget
		case "DCC1003":  // 2x DC Motor Phidget
			try {
				return (float)(((DCMotor)device).getMinBrakingStrength());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get minimum breaking strength for device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("getMinBrakingStrength() is not valid for device of type " + deviceType);	
			break;
		}
		return 0;
	}
	
	@Override
	public float getMaxBrakingStrength() {
		switch (deviceType) {
		case "DCC1000":  // DC Motor Phidget
		case "DCC1001":  // 2A DC Motor Phidget
		case "DCC1002":  // 4A DC Motor Phidget
		case "DCC1003":  // 2x DC Motor Phidget
			try {
				return (float)(((DCMotor)device).getMaxBrakingStrength());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get maximum breaking strength for device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("getMaxBrakingStrength() is not valid for device of type " + deviceType);	
			break;
		}
		return 0;
	}

	@Override
	public float getTargetBrakingStrength() {
		switch (deviceType) {
		case "1065":  // PhidgetMotorControl 1-Motor
		case "DCC1000":  // DC Motor Phidget
		case "DCC1001":  // 2A DC Motor Phidget
		case "DCC1002":  // 4A DC Motor Phidget
		case "DCC1003":  // 2x DC Motor Phidget
			try {
				return (float)(((DCMotor)device).getTargetBrakingStrength());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get target breaking strength for device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("getTargetBrakingStrength() is not valid for device of type " + deviceType);	
			break;
		}
		return 0;
	}
	
	@Override
	public void setTargetBrakingStrength(float strength) {
		switch (deviceType) {
		case "1065":  // PhidgetMotorControl 1-Motor
		case "DCC1000":  // DC Motor Phidget
		case "DCC1001":  // 2A DC Motor Phidget
		case "DCC1002":  // 4A DC Motor Phidget
		case "DCC1003":  // 2x DC Motor Phidget
			try {
				((DCMotor)device).setTargetBrakingStrength((double)strength);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set target breaking strength to device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("setTargetBrakingStrength(float) is not valid for device of type " + deviceType);	
			break;
		}
	}
	
	@Override
	public float getCurrentLimit() {
		switch (deviceType) {
		case "DCC1000":  // DC Motor Phidget
		case "DCC1001":  // 2A DC Motor Phidget
		case "DCC1002":  // 4A DC Motor Phidget
		case "DCC1003":  // 2x DC Motor Phidget
			try {
				return (float)(((DCMotor)device).getCurrentLimit());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get current limit for device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("getCurrentLimit() is not valid for device of type " + deviceType);	
			break;
		}
		return 0;
	}

	@Override
	public void setCurrentLimit(float curr) {
		switch (deviceType) {
		case "DCC1000":  // DC Motor Phidget
		case "DCC1001":  // 2A DC Motor Phidget
		case "DCC1002":  // 4A DC Motor Phidget
		case "DCC1003":  // 2x DC Motor Phidget
			try {
				((DCMotor)device).setCurrentLimit((double)curr);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set current limit to device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("getCurrentLimit() is not valid for device of type " + deviceType);	
			break;
		}
	}

	@Override
	public float getMinCurrentLimit() {
		switch (deviceType) {
		case "DCC1000":  // DC Motor Phidget
		case "DCC1001":  // 2A DC Motor Phidget
		case "DCC1002":  // 4A DC Motor Phidget
		case "DCC1003":  // 2x DC Motor Phidget
			try {
				return (float)(((DCMotor)device).getMinCurrentLimit());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get min current limit for device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("getCurrentLimit() is not valid for device of type " + deviceType);	
			break;
		}
		return 0;
	}

	@Override
	public float getMaxCurrentLimit() {
		switch (deviceType) {
		case "DCC1000":  // DC Motor Phidget
		case "DCC1001":  // 2A DC Motor Phidget
		case "DCC1002":  // 4A DC Motor Phidget
		case "DCC1003":  // 2x DC Motor Phidget
			try {
				return (float)(((DCMotor)device).getMaxCurrentLimit());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get max current limit for device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("getCurrentLimit() is not valid for device of type " + deviceType);	
			break;
		}
		return 0;
	}
	
	@Override
	public float getCurrentRegulatorGain() {
		if (deviceType.equals("DCC1000")) {
			try {
				return (float)(((DCMotor)device).getCurrentRegulatorGain());
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
				((DCMotor)device).setCurrentRegulatorGain((double)gain);
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
				return (float)(((DCMotor)device).getMinCurrentRegulatorGain());
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
				return (float)(((DCMotor)device).getMaxCurrentRegulatorGain());
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
			return ((DCMotor)device).getDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setDataInterval(int dataInterval) {
		try {
			((DCMotor)device).setDataInterval(dataInterval);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set data interval value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public int getMinDataInterval() {
		try {
			return ((DCMotor)device).getMinDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public int getMaxDataInterval() {
		try {
			return ((DCMotor)device).getMaxDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	/*
	 * enables the failsafe feature for the channel, with a given failsafe time
	 * 
	 * @param failsafeTime time to enter fail safe mode (milliseconds)
	 */
	@Override
	public void enableFailsafe(int failsafeTime) {
		switch (deviceType) {
		case "DCC1000":  // DC Motor Phidget
		case "DCC1001":  // 2A DC Motor Phidget
		case "DCC1002":  // 4A DC Motor Phidget
		case "DCC1003":  // 2x DC Motor Phidget
			try {
				((DCMotor)device).enableFailsafe(failsafeTime);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set failsafe for device " + deviceType + " because of error: " + ex);
				PAppletParent.exit();
			}
			break;

		default:
			System.err.println("enableFailsafe(int) is not valid for device of type " + deviceType);	
			PAppletParent.exit();
			break;
		}
	}	

	/*
	 * The minimum value that failsafe time can be set to.
	 * 
	 * @return int
	 */
	@Override
	public int getMinFailsafeTime() {
		switch (deviceType) {
		case "DCC1000":  // DC Motor Phidget
		case "DCC1001":  // 2A DC Motor Phidget
		case "DCC1002":  // 4A DC Motor Phidget
		case "DCC1003":  // 2x DC Motor Phidget
			try {
				return ((DCMotor)device).getMinFailsafeTime();
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get min falesafe time from device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("getMinFailsafeTime() is not valid for device of type " + deviceType);				
			break;
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
		switch (deviceType) {
		case "DCC1000":  // DC Motor Phidget
		case "DCC1001":  // 2A DC Motor Phidget
		case "DCC1002":  // 4A DC Motor Phidget
		case "DCC1003":  // 2x DC Motor Phidget
			try {
				return ((DCMotor)device).getMaxFailsafeTime();
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get max falesafe time from device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("getMaxFailsafeTime() is not valid for device of type " + deviceType);				
			break;
		}
		return 0;
	}

	/*
	 * resets the failsafe timer
	 * 
	 */
	@Override
	public void resetFailsafe() {
		switch (deviceType) {
		case "DCC1000":  // DC Motor Phidget
		case "DCC1001":  // 2A DC Motor Phidget
		case "DCC1002":  // 4A DC Motor Phidget
		case "DCC1003":  // 2x DC Motor Phidget
			try {
				((DCMotor)device).resetFailsafe();
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot reset failsafe timer for device " + deviceType + " because of error: " + ex);
				PAppletParent.exit();
			}
			break;

		default:
			System.err.println("resetFailsafe() is not valid for device of type " + deviceType);				
			PAppletParent.exit();
			break;
		}
	}	
	
	@Override
	public String getFanMode() {
		if (deviceType.equals("DCC1000")) {
			try {
				switch (((DCMotor)device).getFanMode()) {
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
					((DCMotor)device).setFanMode(FanMode.ON);
					break;
	
				case "OFF":
					((DCMotor)device).setFanMode(FanMode.OFF);
					break;
	
				case "AUTO":
					((DCMotor)device).setFanMode(FanMode.AUTO);
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
	public float getVelocity() {
		try {
			return (float)(((DCMotor)device).getVelocity());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get velocity value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0;
	}
	
	@Override
	public float getMinVelocity() {
		try {
			return (float)(((DCMotor)device).getMinVelocity());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min velocity value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}
	
	@Override
	public float getMaxVelocity() {
		try {
			return (float)(((DCMotor)device).getMaxVelocity());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max velocity value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}
}
