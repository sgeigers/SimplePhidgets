package shenkar.SimplePhidgets;

import processing.core.*;
import java.lang.reflect.Method;
import com.phidget22.*;

public class P_RC_Servo extends Device {
	boolean firstCall = true;  // for minimalistic use - users will only call setAngle. In this case - it should set maximum velocity and acceleration

	// events
	Method positionChangeEventMethod;  // positionChange
	Method velocityChangeEventMethod;  // velocityChange
	Method targetReachedEventMethod;   // targetReached
	boolean positionChangeFlag = false;
	boolean velocityChangeFlag = false;
	boolean targetReachedFlag = false;
	boolean positionChangeEventReportChannel = false;
	boolean velocityChangeEventReportChannel = false;
	boolean targetReachedEventReportChannel = false;

	// real-time events
	Method positionChangeEventRTMethod;  // positionChangeRT
	Method velocityChangeEventRTMethod;  // velocityChangeRT
	Method targetReachedEventRTMethod;   // targetReachedRT

	public P_RC_Servo(PApplet P5Parent, Channel ChParent, String type, int serialNum, int portNum, int chNum) {
		super(P5Parent, ChParent, type, serialNum, portNum, chNum);

		// pre-opening setup
		try {
			device = new RCServo();
		}	catch (PhidgetException ex) {
			System.err.println("Could not open device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
		}

		// device opening
		switch (deviceType) {
		case "1000":   // PhidgetServo 1-Motor
		case "1001":   // PhidgetServo 4-Motor
		case "1061":   // PhidgetAdvancedServo 8-Motor
		case "1066":   // PhidgetAdvancedServo 1-Motor
		case "RCC0004":  // PhidgetAdvancedServo 8-Motor
			initNoHub();
			break;

		case "RCC1000":  // 16x RC Servo Phidget
			init(false);
			break;

		default:
			System.err.println("Could not open device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
			break;
		}

		// post-opening setup
		attachListeners();
	}

	// check if "positionChange()", "velocityChange()" or "targetReached()" were defined in the sketch and create a listener for it.
	void attachListeners() {
		// positionChange()
		try {
			positionChangeEventMethod =  PAppletParent.getClass().getMethod("positionChange");
			if (positionChangeEventMethod != null) {
				positionChangeEventReportChannel = false;
				((RCServo)device).addPositionChangeListener(new RCServoPositionChangeListener() {
					public void onPositionChange(RCServoPositionChangeEvent  e) {
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
				((RCServo)device).addPositionChangeListener(new RCServoPositionChangeListener() {
					public void onPositionChange(RCServoPositionChangeEvent e) {
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
					((RCServo)device).addPositionChangeListener(new RCServoPositionChangeListener() {
						public void onPositionChange(RCServoPositionChangeEvent e) {
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
					((RCServo)device).addPositionChangeListener(new RCServoPositionChangeListener() {
						public void onPositionChange(RCServoPositionChangeEvent e) {
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

		switch (deviceType) {
		case "1061":   // PhidgetAdvancedServo 8-Motor
		case "1066":   // PhidgetAdvancedServo 1-Motor
		case "RCC0004":  // PhidgetAdvancedServo 8-Motor
		case "RCC1000":  // 16x RC Servo Phidget
			
			// velocityChange()
			try {
				velocityChangeEventMethod =  PAppletParent.getClass().getMethod("velocityChange");
				if (velocityChangeEventMethod != null) {
					velocityChangeEventReportChannel = false;
					((RCServo)device).addVelocityChangeListener(new RCServoVelocityChangeListener() {
						public void onVelocityChange(RCServoVelocityChangeEvent   e) {
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
					((RCServo)device).addVelocityChangeListener(new RCServoVelocityChangeListener() {
						public void onVelocityChange(RCServoVelocityChangeEvent e) {
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
						((RCServo)device).addVelocityChangeListener(new RCServoVelocityChangeListener() {
							public void onVelocityChange(RCServoVelocityChangeEvent e) {
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
						((RCServo)device).addVelocityChangeListener(new RCServoVelocityChangeListener() {
							public void onVelocityChange(RCServoVelocityChangeEvent e) {
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
			
			// targetReached()
			try {
				targetReachedEventMethod =  PAppletParent.getClass().getMethod("targetReached");
				if (targetReachedEventMethod != null) {
					targetReachedEventReportChannel = false;
					((RCServo)device).addTargetPositionReachedListener(new RCServoTargetPositionReachedListener() {
						public void onTargetPositionReached(RCServoTargetPositionReachedEvent e) {
							//System.out.println(e.toString());
							targetReachedFlag = true;
						}
					});
				}
			} catch (Exception e) {
				// function "targetReached" not defined
			}

			// targetReached(Channel)
			try {
				targetReachedEventMethod =  PAppletParent.getClass().getMethod("targetReached", new Class<?>[] { Channel.class });
				if (targetReachedEventMethod != null) {
					targetReachedEventReportChannel = true;
					((RCServo)device).addTargetPositionReachedListener(new RCServoTargetPositionReachedListener() {
						public void onTargetPositionReached(RCServoTargetPositionReachedEvent e) {
							//System.out.println(e.toString());
							targetReachedFlag = true;
						}
					});
				}
			} catch (Exception e) {
				// function "targetReached(Channel)" not defined
			}

			//  targetReachedRT()
			try {
				 targetReachedEventRTMethod =  PAppletParent.getClass().getMethod(" targetReachedRT");
				if (targetReachedEventRTMethod != null) {
					if (velocityChangeEventMethod != null) {
						System.err.println("Cannot use both velocityChange() and velocityChangeRT()."); 
					}
					else {
						((RCServo)device).addTargetPositionReachedListener(new RCServoTargetPositionReachedListener() {
							public void onTargetPositionReached(RCServoTargetPositionReachedEvent e) {
								//System.out.println(e.toString());
								try {
									if (targetReachedEventRTMethod != null) {
										targetReachedEventRTMethod.invoke(PAppletParent);
									}
								} catch (Exception ex) {
									System.err.println("Disabling targetReachedRT() for " + deviceType + " because of an error:");
									ex.printStackTrace();
									targetReachedEventRTMethod = null;
								}
							}
						});
					}
				}
			} catch (Exception e) {
				// function "targetReachedRT()" not defined
			}

			//  targetReachedRT(Channel)
			try {
				 targetReachedEventRTMethod =  PAppletParent.getClass().getMethod(" targetReachedRT", new Class<?>[] { Channel.class });
				if (targetReachedEventRTMethod != null) {
					if (velocityChangeEventMethod != null) {
						System.err.println("Cannot use both velocityChange() and velocityChangeRT()."); 
					}
					else {
						((RCServo)device).addTargetPositionReachedListener(new RCServoTargetPositionReachedListener() {
							public void onTargetPositionReached(RCServoTargetPositionReachedEvent e) {
								//System.out.println(e.toString());
								try {
									if (targetReachedEventRTMethod != null) {
										targetReachedEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
									}
								} catch (Exception ex) {
									System.err.println("Disabling targetReachedRT() for " + deviceType + " because of an error:");
									ex.printStackTrace();
									targetReachedEventRTMethod = null;
								}
							}
						});
					}
				}
			} catch (Exception e) {
				// function "targetReachedRT(Channel)" not defined
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

		if (targetReachedFlag) {
			targetReachedFlag = false;
			try {
				if (targetReachedEventMethod != null) {
					if (targetReachedEventReportChannel) {
						targetReachedEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						targetReachedEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling targetReached() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				targetReachedEventMethod = null;
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
				((RCServo)device).setEngaged(false);					
				device.close();
				System.out.println(deviceType + " Closed");
			}
			catch (PhidgetException ex) {
				System.err.println("Could not close device " + deviceType + ". See github.com/sgeigers/SimplePhidgets#reference for help");
			}
		}
	}

	/*
	 * most basic way to use the channel. Turns the servo to desired angle
	 * 
	 */
	@Override
	public void setAngle(float ang) {
		try {
			((RCServo)device).setTargetPosition((double)ang);
			if (((RCServo)device).getEngaged() == false) {
				if (firstCall) {
					((RCServo)device).setVelocityLimit(((RCServo)device).getMaxVelocityLimit());
					((RCServo)device).setAcceleration(((RCServo)device).getMaxAcceleration());
					firstCall = false;
				}
				((RCServo)device).setEngaged(true);
			}
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set device " + deviceType + " to ON state because of error: " + ex);
		}
	}

	@Override
	public float getAcceleration() {
		switch (deviceType) {
		case "1061":   // PhidgetAdvancedServo 8-Motor
		case "1066":   // PhidgetAdvancedServo 1-Motor
		case "RCC0004":  // PhidgetAdvancedServo 8-Motor
		case "RCC1000":  // 16x RC Servo Phidget
			try {
				return (float)(((RCServo)device).getAcceleration());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get acceleration value from device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("getAcceleration() is not valid for device of type " + deviceType);	
			break;
		}
		return 0;
	}

	@Override
	public void setAcceleration(float accel) {
		switch (deviceType) {
		case "1061":   // PhidgetAdvancedServo 8-Motor
		case "1066":   // PhidgetAdvancedServo 1-Motor
		case "RCC0004":  // PhidgetAdvancedServo 8-Motor
		case "RCC1000":  // 16x RC Servo Phidget
			try {
				((RCServo)device).setAcceleration((double)accel);
				firstCall = false;
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set acceleration value to device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("setAcceleration(float) is not valid for device of type " + deviceType);	
			break;
		}
	}

	@Override
	public float getMinAcceleration() {
		switch (deviceType) {
		case "1061":   // PhidgetAdvancedServo 8-Motor
		case "1066":   // PhidgetAdvancedServo 1-Motor
		case "RCC0004":  // PhidgetAdvancedServo 8-Motor
		case "RCC1000":  // 16x RC Servo Phidget
			try {
				return (float)(((RCServo)device).getMinAcceleration());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get min acceleration value from device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("getMinAcceleration() is not valid for device of type " + deviceType);	
			break;
		}
		return 0;
	}

	@Override
	public float getMaxAcceleration() {
		switch (deviceType) {
		case "1061":   // PhidgetAdvancedServo 8-Motor
		case "1066":   // PhidgetAdvancedServo 1-Motor
		case "RCC0004":  // PhidgetAdvancedServo 8-Motor
		case "RCC1000":  // 16x RC Servo Phidget
			try {
				return (float)(((RCServo)device).getMaxAcceleration());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get max acceleration value from device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("getMaxAcceleration() is not valid for device of type " + deviceType);	
			break;
		}
		return 0;
	}

	@Override
	public int getDataInterval() {
		switch (deviceType) {
		case "1061":   // PhidgetAdvancedServo 8-Motor
		case "1066":   // PhidgetAdvancedServo 1-Motor
		case "RCC0004":  // PhidgetAdvancedServo 8-Motor
		case "RCC1000":  // 16x RC Servo Phidget
			try {
				return ((RCServo)device).getDataInterval();
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get data interval value from device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("getDataInterval() is not valid for device of type " + deviceType);	
			break;
		}
		return 0;
	}

	@Override
	public void setDataInterval(int dataInterval) {
		switch (deviceType) {
		case "1061":   // PhidgetAdvancedServo 8-Motor
		case "1066":   // PhidgetAdvancedServo 1-Motor
		case "RCC0004":  // PhidgetAdvancedServo 8-Motor
		case "RCC1000":  // 16x RC Servo Phidget
			try {
				((RCServo)device).setDataInterval(dataInterval);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set data interval value to device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("setDataInterval(int) is not valid for device of type " + deviceType);	
			break;
		}
	}

	@Override
	public int getMinDataInterval() {
		switch (deviceType) {
		case "1061":   // PhidgetAdvancedServo 8-Motor
		case "1066":   // PhidgetAdvancedServo 1-Motor
		case "RCC0004":  // PhidgetAdvancedServo 8-Motor
		case "RCC1000":  // 16x RC Servo Phidget
			try {
				return ((RCServo)device).getMinDataInterval();
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get min data interval value from device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("getMinDataInterval() is not valid for device of type " + deviceType);	
			break;
		}
		return 0;
	}

	@Override
	public int getMaxDataInterval() {
		switch (deviceType) {
		case "1061":   // PhidgetAdvancedServo 8-Motor
		case "1066":   // PhidgetAdvancedServo 1-Motor
		case "RCC0004":  // PhidgetAdvancedServo 8-Motor
		case "RCC1000":  // 16x RC Servo Phidget
			try {
				return ((RCServo)device).getMaxDataInterval();
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get max data interval value from device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("getMaxDataInterval() is not valid for device of type " + deviceType);	
			break;
		}
		return 0;
	}

	@Override
	public boolean getEngaged() {
		try {
			return ((RCServo)device).getEngaged();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get engaged state from device " + deviceType + " because of error: " + ex);
		}
		return false;
	}

	@Override
	public void setEngaged(boolean eng) {
		try {
			((RCServo)device).setEngaged(eng);
			firstCall = false;
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
		case "RCC1000":  // 16x RC Servo Phidget
			try {
				((RCServo)device).enableFailsafe(failsafeTime);
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
		case "RCC1000":  // 16x RC Servo Phidget
			try {
				return ((RCServo)device).getMinFailsafeTime();
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
		case "RCC1000":  // 16x RC Servo Phidget
			try {
				return ((RCServo)device).getMaxFailsafeTime();
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
		case "RCC1000":  // 16x RC Servo Phidget
			try {
				((RCServo)device).resetFailsafe();
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
	public boolean getIsMoving() {
		switch (deviceType) {
		case "1061":   // PhidgetAdvancedServo 8-Motor
		case "1066":   // PhidgetAdvancedServo 1-Motor
		case "RCC0004":  // PhidgetAdvancedServo 8-Motor
		case "RCC1000":  // 16x RC Servo Phidget
			try {
				return ((RCServo)device).getIsMoving();
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get \"is moving\" state from device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("getIsMoving() is not valid for device of type " + deviceType);				
			break;
		}
		return false;
	}

	@Override
	public float getPosition() {
		try {
			return (float)(((RCServo)device).getPosition());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get position value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setMinPosition(float pos) {
		try {
			((RCServo)device).setMinPosition((double)pos);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set min position value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getMinPosition() {
		try {
			return (float)(((RCServo)device).getMinPosition());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min position value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setMaxPosition(float pos) {
		try {
			((RCServo)device).setMaxPosition((double)pos);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set max position value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getMaxPosition() {
		try {
			return (float)(((RCServo)device).getMaxPosition());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max position value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setMinPulseWidth(float pls) {
		try {
			((RCServo)device).setMinPulseWidth((double)pls);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set min pulse width value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getMinPulseWidth() {
		try {
			return (float)(((RCServo)device).getMinPulseWidth());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min pulse width value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setMaxPulseWidth(float pls) {
		try {
			((RCServo)device).setMaxPulseWidth((double)pls);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set max pulse width value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getMaxPulseWidth() {
		try {
			return (float)(((RCServo)device).getMaxPulseWidth());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max pulse width value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public float getMinPulseWidthLimit() {
		try {
			return (float)(((RCServo)device).getMinPulseWidthLimit());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min pulse width limit value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}
	
	@Override
	public float getMaxPulseWidthLimit() {
		try {
			return (float)(((RCServo)device).getMaxPulseWidthLimit());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max pulse width limit value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public boolean getSpeedRampingState() {
		try {
			return ((RCServo)device).getSpeedRampingState();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get speed ramping state from device " + deviceType + " because of error: " + ex);
		}
		return false;
	}

	@Override
	public void setSpeedRampingState(boolean state) {
		switch (deviceType) {
		case "1061":   // PhidgetAdvancedServo 8-Motor
		case "1066":   // PhidgetAdvancedServo 1-Motor
		case "RCC0004":  // PhidgetAdvancedServo 8-Motor
		case "RCC1000":  // 16x RC Servo Phidget
			try {
				((RCServo)device).setSpeedRampingState(state);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set speed ramping state from device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("setSpeedRampingState(boolean) is not valid for device of type " + deviceType);				
			break;
		}
	}

	@Override
	public float getTargetPosition() {
		try {
			return (float)(((RCServo)device).getTargetPosition());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get target position from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setTargetPosition(float tgt) {
		try {
			((RCServo)device).setTargetPosition((double)tgt);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set target position to device " + deviceType + " because of error: " + ex);
		}
	}

	// All torque functions have no board using them currently, so not implemented
	/*
	@Override
	public float getTorque() {
	}

	@Override
	public void setTorque(float trq) {
	}

	@Override
	public float getMinTorque() {
	}

	@Override
	public float getMaxTorque() {
	}
	*/
	
	@Override
	public float getVelocity() {
		switch (deviceType) {
		case "1061":   // PhidgetAdvancedServo 8-Motor
		case "1066":   // PhidgetAdvancedServo 1-Motor
		case "RCC0004":  // PhidgetAdvancedServo 8-Motor
		case "RCC1000":  // 16x RC Servo Phidget
			try {
				return (float)(((RCServo)device).getVelocity());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get velocity value from device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("getVelocity() is not valid for device of type " + deviceType);				
			break;
		}
		return 0;
	}
	
	@Override
	public float getVelocityLimit() {
		switch (deviceType) {
		case "1061":   // PhidgetAdvancedServo 8-Motor
		case "1066":   // PhidgetAdvancedServo 1-Motor
		case "RCC0004":  // PhidgetAdvancedServo 8-Motor
		case "RCC1000":  // 16x RC Servo Phidget
			try {
				return (float)(((RCServo)device).getVelocityLimit());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get velocity limit value from device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("getVelocityLimit() is not valid for device of type " + deviceType);				
			break;
		}
		return 0;
	}
	
	@Override
	public void setVelocityLimit(float vel) {
		switch (deviceType) {
		case "1061":   // PhidgetAdvancedServo 8-Motor
		case "1066":   // PhidgetAdvancedServo 1-Motor
		case "RCC0004":  // PhidgetAdvancedServo 8-Motor
		case "RCC1000":  // 16x RC Servo Phidget
			try {
				((RCServo)device).setVelocityLimit((double)vel);
				firstCall = false;
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set velocity limit value to device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("setVelocityLimit(float) is not valid for device of type " + deviceType);				
			break;
		}
	}
	
	@Override
	public float getMinVelocityLimit() {
		switch (deviceType) {
		case "1061":   // PhidgetAdvancedServo 8-Motor
		case "1066":   // PhidgetAdvancedServo 1-Motor
		case "RCC0004":  // PhidgetAdvancedServo 8-Motor
		case "RCC1000":  // 16x RC Servo Phidget
			try {
				return (float)(((RCServo)device).getMinVelocityLimit());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get min velocity limit value from device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("getMinVelocityLimit() is not valid for device of type " + deviceType);				
			break;
		}
		return 0;
	}
	
	@Override
	public float getMaxVelocityLimit() {
		switch (deviceType) {
		case "1061":   // PhidgetAdvancedServo 8-Motor
		case "1066":   // PhidgetAdvancedServo 1-Motor
		case "RCC0004":  // PhidgetAdvancedServo 8-Motor
		case "RCC1000":  // 16x RC Servo Phidget
			try {
				return (float)(((RCServo)device).getMaxVelocityLimit());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get max velocity limit value from device " + deviceType + " because of error: " + ex);
			}
			break;

		default:
			System.err.println("getMaxVelocityLimit() is not valid for device of type " + deviceType);				
			break;
		}
		return 0;
	}
	
	@Override
	public float getVoltage() {
		System.out.println("For servo controllers, use \"getVoltageString\" instead of \"getVoltage\"");
		return 0;
	}
	
	@Override
	public String getVoltageString() {
		try {
			switch (((RCServo)device).getVoltage()) {
			case VOLTS_5_0:
				return "5.0";

			case VOLTS_6_0:
				return "6.0";

			case VOLTS_7_4:
				return "7.4";
			}
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get voltage from device " + deviceType + " because of error: " + ex);
		}
		return "";
	}
	
	@Override
	public void setVoltage(String vol) {
		try {
			switch (vol) {
			case "5":
			case "5.0":
				((RCServo)device).setVoltage(RCServoVoltage.VOLTS_5_0);
				break;

			case "6":
			case "6.0":
				((RCServo)device).setVoltage(RCServoVoltage.VOLTS_6_0);
				break;

			case "7":
			case "7.4":
				((RCServo)device).setVoltage(RCServoVoltage.VOLTS_7_4);
				break;

			default:
				System.err.println("Invalid servo voltage: " + vol + ". Use only \"5.0\", \"6.0\" or \"7.4\"");			
			}
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set voltage to device " + deviceType + " because of error: " + ex);
		}
	}
}
