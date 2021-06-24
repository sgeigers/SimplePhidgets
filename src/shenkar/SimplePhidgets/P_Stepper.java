package shenkar.SimplePhidgets;

import processing.core.*;
import java.lang.reflect.Method;
import com.phidget22.*;

public class P_Stepper extends Device {
	// events
	Method positionChangeEventMethod;  // positionChange
	Method velocityChangeEventMethod;  // velocityChange
	Method stoppedEventMethod;   // stopped
	boolean positionChangeFlag = false;
	boolean velocityChangeFlag = false;
	boolean stoppedFlag = false;
	boolean positionChangeEventReportChannel = false;
	boolean velocityChangeEventReportChannel = false;
	boolean stoppedEventReportChannel = false;

	// real-time events
	Method positionChangeEventRTMethod;  // positionChangeRT
	Method velocityChangeEventRTMethod;  // velocityChangeRT
	Method stoppedEventRTMethod;   // stoppedRT

	public P_Stepper(PApplet P5Parent, Channel ChParent, String type, int serialNum, int portNum, int chNum) {
		super(P5Parent, ChParent, type, serialNum, portNum, chNum);

		// pre-opening setup
		try {
			device = new Stepper();
		}	catch (PhidgetException ex) {
			System.err.println("Could not open device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
		}

		// device opening
		switch (deviceType) {
		case "1062":   // PhidgetStepper Unipolar 4-Motor
		case "1063":   // PhidgetStepper Bipolar 1-Motor
		case "1067":   // PhidgetStepper Bipolar HC
			initNoHub();
			break;

		case "STC1000":  // Stepper Phidget
		case "STC1001":  // 2.5A Stepper Phidget
		case "STC1002":  // 8A Stepper Phidget
		case "STC1003":  // 4A Stepper Phidget
			init(false);
			break;

		default:
			System.err.println("Could not open device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
			break;
		}

		// post-opening setup
		attachListeners();
	}

	// check if "positionChange()", "velocityChange()" or "stopped()" were defined in the sketch and create a listener for it.
	void attachListeners() {
		// positionChange()
		try {
			positionChangeEventMethod =  PAppletParent.getClass().getMethod("positionChange");
			if (positionChangeEventMethod != null) {
				positionChangeEventReportChannel = false;
				((Stepper)device).addPositionChangeListener(new StepperPositionChangeListener() {
					public void onPositionChange(StepperPositionChangeEvent  e) {
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
				((Stepper)device).addPositionChangeListener(new StepperPositionChangeListener() {
					public void onPositionChange(StepperPositionChangeEvent e) {
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
					((Stepper)device).addPositionChangeListener(new StepperPositionChangeListener() {
						public void onPositionChange(StepperPositionChangeEvent e) {
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
					((Stepper)device).addPositionChangeListener(new StepperPositionChangeListener() {
						public void onPositionChange(StepperPositionChangeEvent e) {
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
			}
		} catch (Exception e) {
			// function "positionChangeRT()" not defined
		}

		// velocityChange()
		try {
			velocityChangeEventMethod =  PAppletParent.getClass().getMethod("velocityChange");
			if (velocityChangeEventMethod != null) {
				velocityChangeEventReportChannel = false;
				((Stepper)device).addVelocityChangeListener(new StepperVelocityChangeListener() {
					public void onVelocityChange(StepperVelocityChangeEvent   e) {
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
				((Stepper)device).addVelocityChangeListener(new StepperVelocityChangeListener() {
					public void onVelocityChange(StepperVelocityChangeEvent e) {
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
					((Stepper)device).addVelocityChangeListener(new StepperVelocityChangeListener() {
						public void onVelocityChange(StepperVelocityChangeEvent e) {
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
					((Stepper)device).addVelocityChangeListener(new StepperVelocityChangeListener() {
						public void onVelocityChange(StepperVelocityChangeEvent e) {
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
			}
		} catch (Exception e) {
			// function "velocityChangeRT(Channel)" not defined
		}
		
		// stopped()
		try {
			stoppedEventMethod =  PAppletParent.getClass().getMethod("stopped");
			if (stoppedEventMethod != null) {
				stoppedEventReportChannel = false;
				((Stepper)device).addStoppedListener(new StepperStoppedListener() {
					public void onStopped(StepperStoppedEvent e) {
						//System.out.println(e.toString());
						stoppedFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "stopped" not defined
		}

		// stopped(Channel)
		try {
			stoppedEventMethod =  PAppletParent.getClass().getMethod("stopped", new Class<?>[] { Channel.class });
			if (stoppedEventMethod != null) {
				stoppedEventReportChannel = true;
				((Stepper)device).addStoppedListener(new StepperStoppedListener() {
					public void onStopped(StepperStoppedEvent e) {
						//System.out.println(e.toString());
						stoppedFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "stopped(Channel)" not defined
		}

		//  stoppedRT()
		try {
			 stoppedEventRTMethod =  PAppletParent.getClass().getMethod(" stoppedRT");
			if (stoppedEventRTMethod != null) {
				if (velocityChangeEventMethod != null) {
					System.err.println("Cannot use both velocityChange() and velocityChangeRT()."); 
				}
				else {
					((Stepper)device).addStoppedListener(new StepperStoppedListener() {
						public void onStopped(StepperStoppedEvent e) {
							//System.out.println(e.toString());
							try {
								if (stoppedEventRTMethod != null) {
									stoppedEventRTMethod.invoke(PAppletParent);
								}
							} catch (Exception ex) {
								System.err.println("Disabling stoppedRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								stoppedEventRTMethod = null;
							}
						}
					});
				}
			}
		} catch (Exception e) {
			// function "stoppedRT()" not defined
		}

		//  stoppedRT(Channel)
		try {
			 stoppedEventRTMethod =  PAppletParent.getClass().getMethod(" stoppedRT", new Class<?>[] { Channel.class });
			if (stoppedEventRTMethod != null) {
				if (velocityChangeEventMethod != null) {
					System.err.println("Cannot use both velocityChange() and velocityChangeRT()."); 
				}
				else {
					((Stepper)device).addStoppedListener(new StepperStoppedListener() {
						public void onStopped(StepperStoppedEvent e) {
							//System.out.println(e.toString());
							try {
								if (stoppedEventRTMethod != null) {
									stoppedEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
								}
							} catch (Exception ex) {
								System.err.println("Disabling stoppedRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								stoppedEventRTMethod = null;
							}
						}
					});
				}
			}
		} catch (Exception e) {
			// function "stoppedRT(Channel)" not defined
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

		if (stoppedFlag) {
			stoppedFlag = false;
			try {
				if (stoppedEventMethod != null) {
					if (stoppedEventReportChannel) {
						stoppedEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						stoppedEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling stopped() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				stoppedEventMethod = null;
			}
		}
}

	/*
	 * disengages all motors before closing
	 * 
	 */
	@Override
	public void close() {
		if (device != null) {
			try {
				((Stepper)device).setEngaged(false);					
				device.close();
				System.out.println(deviceType + " Closed");
			}
			catch (PhidgetException ex) {
				System.err.println("Could not close device " + deviceType + ". See github.com/sgeigers/SimplePhidgets#reference for help");
			}
		}
	}

	/*
	 * most basic way to use the channel. Turns the stepper to desired position
	 * 
	 */
	@Override
	public void setTargetPosition(float pos) {
		try {
			if (((Stepper)device).getControlMode() != StepperControlMode.STEP) {
				((Stepper)device).setControlMode(StepperControlMode.STEP);
			}
			if (((Stepper)device).getEngaged() == false) {
				((Stepper)device).setEngaged(true);
			}
			((Stepper)device).setTargetPosition((double)pos);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set target position to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getAcceleration() {
		try {
			return (float)(((Stepper)device).getAcceleration());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get acceleration value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setAcceleration(float accel) {
		try {
			((Stepper)device).setAcceleration((double)accel);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set acceleration value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getMinAcceleration() {
		try {
			return (float)(((Stepper)device).getMinAcceleration());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min acceleration value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public float getMaxAcceleration() {
		try {
			return (float)(((Stepper)device).getMaxAcceleration());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max acceleration value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public String getControlMode() {
		try {
			switch (((Stepper)device).getControlMode()) {
			case STEP:
				return "STEP";

			case RUN:
				return "RUN";
			}
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get control mode from device " + deviceType + " because of error: " + ex);
		}
		return "";
	}
	
	@Override
	public void setControlMode(String mode) {
		try {
			switch (mode) {
			case "STEP":
			case "Step":
			case "step":
				((Stepper)device).setControlMode(StepperControlMode.STEP);
				break;

			case "RUN":
			case "Run":
			case "run":
				((Stepper)device).setControlMode(StepperControlMode.RUN);
				break;

			default:
				System.err.println("Invalid control mode: " + mode + ". Use only \"STEP\" or \"RUN\"");			
			}
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set control mode to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getCurrentLimit() {
		try {
			return (float)(((Stepper)device).getCurrentLimit());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get current limit for device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setCurrentLimit(float curr) {
		try {
			((Stepper)device).setCurrentLimit((double)curr);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set current limit to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getMinCurrentLimit() {
		try {
			return (float)(((Stepper)device).getMinCurrentLimit());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min current limit for device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public float getMaxCurrentLimit() {
		try {
			return (float)(((Stepper)device).getMaxCurrentLimit());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max current limit for device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}
		
	@Override
	public int getDataInterval() {
		try {
			return ((Stepper)device).getDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setDataInterval(int dataInterval) {
		try {
			((Stepper)device).setDataInterval(dataInterval);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set data interval value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public int getMinDataInterval() {
		try {
			return ((Stepper)device).getMinDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public int getMaxDataInterval() {
		try {
			return ((Stepper)device).getMaxDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public boolean getEngaged() {
		try {
			return ((Stepper)device).getEngaged();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get engaged state from device " + deviceType + " because of error: " + ex);
		}
		return false;
	}

	@Override
	public void setEngaged(boolean eng) {
		try {
			((Stepper)device).setEngaged(eng);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set engaged state to device " + deviceType + " because of error: " + ex);
		}
	}
	
	/*
	 * enables the failsafe feature for the channel, with a given failsafe time
	 * 
	 * @param failsafeTime time to enter fail safe mode (milliseconds)
	 */
	@Override
	public void enableFailsafe(int failsafeTime) {
		switch (deviceType) {
		case "STC1000":  // Stepper Phidget
		case "STC1001":  // 2.5A Stepper Phidget
		case "STC1002":  // 8A Stepper Phidget
		case "STC1003":  // 4A Stepper Phidget
			try {
				((Stepper)device).enableFailsafe(failsafeTime);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set failsafe for device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("enableFailsafe(int) is not valid for device of type " + deviceType);	
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
		case "STC1000":  // Stepper Phidget
		case "STC1001":  // 2.5A Stepper Phidget
		case "STC1002":  // 8A Stepper Phidget
		case "STC1003":  // 4A Stepper Phidget
			try {
				return ((Stepper)device).getMinFailsafeTime();
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
		case "STC1000":  // Stepper Phidget
		case "STC1001":  // 2.5A Stepper Phidget
		case "STC1002":  // 8A Stepper Phidget
		case "STC1003":  // 4A Stepper Phidget
			try {
				return ((Stepper)device).getMaxFailsafeTime();
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
		case "STC1000":  // Stepper Phidget
		case "STC1001":  // 2.5A Stepper Phidget
		case "STC1002":  // 8A Stepper Phidget
		case "STC1003":  // 4A Stepper Phidget
			try {
				((Stepper)device).resetFailsafe();
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot reset failsafe timer for device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("resetFailsafe() is not valid for device of type " + deviceType);				
			break;
		}
	}	

	@Override
	public float getHoldingCurrentLimit() {
		switch (deviceType) {
		case "STC1000":  // Stepper Phidget
		case "STC1001":  // 2.5A Stepper Phidget
		case "STC1002":  // 8A Stepper Phidget
		case "STC1003":  // 4A Stepper Phidget
			try {
				return (float)(((Stepper)device).getHoldingCurrentLimit());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get holding current limit for device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("getHoldingCurrentLimit() is not valid for device of type " + deviceType);				
			break;
		}
		return 0;
	}

	@Override
	public void setHoldingCurrentLimit(float curr) {
		switch (deviceType) {
		case "STC1000":  // Stepper Phidget
		case "STC1001":  // 2.5A Stepper Phidget
		case "STC1002":  // 8A Stepper Phidget
		case "STC1003":  // 4A Stepper Phidget
			try {
				((Stepper)device).setHoldingCurrentLimit((double)curr);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set holding current limit to device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("setHoldingCurrentLimit() is not valid for device of type " + deviceType);				
			break;
		}
	}

	@Override
	public boolean getIsMoving() {
		try {
			return ((Stepper)device).getIsMoving();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get \"is moving\" state from device " + deviceType + " because of error: " + ex);
		}
		return false;
	}

	@Override
	public float getPosition() {
		try {
			return (float)(((Stepper)device).getPosition());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get position value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public float getMinPosition() {
		try {
			return (float)(((Stepper)device).getMinPosition());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min position value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public float getMaxPosition() {
		try {
			return (float)(((Stepper)device).getMaxPosition());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max position value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void addPositionOffset(int offset) {
		try {
			((Stepper)device).addPositionOffset((double)offset);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot add position offset to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getRescaleFactor() {
		try {
			return (float)(((Stepper)device).getRescaleFactor());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get rescale factor for device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setRescaleFactor(float fctr) {
		try {
			((Stepper)device).setRescaleFactor((double)fctr);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set rescale factor to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getTargetPosition() {
		try {
			return (float)(((Stepper)device).getTargetPosition());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get target Position for device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}
	
	@Override
	public float getVelocity() {
		try {
			return (float)(((Stepper)device).getVelocity());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get velocity value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}
	
	@Override
	public float getVelocityLimit() {
		try {
			return (float)(((Stepper)device).getVelocityLimit());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get velocity limit value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}
	
	@Override
	public void setVelocityLimit(float vel) {
		try {
			((Stepper)device).setVelocityLimit((double)vel);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set velocity limit value to device " + deviceType + " because of error: " + ex);
		}
	}
	
	@Override
	public float getMinVelocityLimit() {
		try {
			return (float)(((Stepper)device).getMinVelocityLimit());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min velocity limit value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}
	
	@Override
	public float getMaxVelocityLimit() {
		try {
			return (float)(((Stepper)device).getMaxVelocityLimit());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max velocity limit value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}
}
