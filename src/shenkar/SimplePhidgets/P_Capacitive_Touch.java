package shenkar.SimplePhidgets;

import processing.core.*;
import java.lang.reflect.Method;
import com.phidget22.*;

public class P_Capacitive_Touch extends Device {
	// events
	Method touchEventMethod;  // capacitiveTouched
	Method touchEndEventMethod; // capacitiveReleased
	boolean touchFlag = false;
	boolean touchEndFlag = false;
	boolean touchEventReportChannel = false;
	boolean touchEndEventReportChannel = false;

	// real-time events
	Method touchEventRTMethod;  // capacitiveTouched
	Method touchEndEventRTMethod; // capacitiveReleased
	boolean RTTouchEventRegister = false;
	boolean RTTouchEndEventRegister = false;
	
	public P_Capacitive_Touch(PApplet P5Parent, Channel ChParent, String type, int serialNum, int portNum, int chNum) {
		super(P5Parent, ChParent, type, serialNum, portNum, chNum);

		// pre-opening setup
		try {
			device = new CapacitiveTouch();
		}	catch (PhidgetException ex) {
			System.err.println("Could not open device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
			PAppletParent.exit();
		}
				
		// device opening
		init(false);

		// post-opening setup
		try {
			// set maximum data rate as default
			((CapacitiveTouch)device).setDataInterval(((CapacitiveTouch)device).getMinDataInterval());
		}	catch (PhidgetException ex) {
			System.err.println("Could not set data interval for device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
		}
		attachListeners();
	}

	// check if "capacitiveTouched()" or "capacitiveReleased()" were defined in the sketch and create listeners for them.
	void attachListeners() {
		
		// capacitiveTouched()
		try {
			touchEventMethod =  PAppletParent.getClass().getMethod("capacitiveTouched");
			if (touchEventMethod != null) {
				touchEventReportChannel = false;
				((CapacitiveTouch)device).addTouchListener(new CapacitiveTouchTouchListener() {
					public void onTouch(CapacitiveTouchTouchEvent e) {
						//System.out.println(e.toString());
						touchFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "capacitiveTouched" not defined
		}

		// capacitiveTouched(Channel)
		try {
			touchEventMethod =  PAppletParent.getClass().getMethod("capacitiveTouched", new Class<?>[] { Channel.class });
			if (touchEventMethod != null) {
				touchEventReportChannel = true;
				((CapacitiveTouch)device).addTouchListener(new CapacitiveTouchTouchListener() {
					public void onTouch(CapacitiveTouchTouchEvent e) {
						//System.out.println(e.toString());
						touchFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "capacitiveTouched" not defined
		}

		// capacitiveTouchedRT()
		try {
			touchEventRTMethod =  PAppletParent.getClass().getMethod("capacitiveTouchedRT");
			if (touchEventRTMethod != null) {
				if (touchEventMethod != null) {
					System.err.println("Cannot use both capacitiveTouched() and capacitiveTouchedRT()."); 
				}
				else {			
					RTTouchEventRegister = true;
					touchEventReportChannel = false;
				}
			}
		} catch (Exception e) {
			// function "capacitiveTouchedRT" not defined
		}

		// capacitiveTouchedRT(Channel)
		try {
			touchEventRTMethod =  PAppletParent.getClass().getMethod("capacitiveTouchedRT", new Class<?>[] { Channel.class });
			if (touchEventRTMethod != null) {
				if (touchEventMethod != null) {
					System.err.println("Cannot use both capacitiveTouched() and capacitiveTouchedRT()."); 
				}
				else {			
					RTTouchEventRegister = true;
					touchEventReportChannel = true;
				}
			}
		} catch (Exception e) {
			// function "capacitiveTouchedRT(channel)" not defined
		}

		// capacitiveReleased()
		try {
			touchEndEventMethod =  PAppletParent.getClass().getMethod("capacitiveReleased");
			if (touchEndEventMethod != null) {
				touchEndEventReportChannel = false;
				((CapacitiveTouch)device).addTouchEndListener(new CapacitiveTouchTouchEndListener() {
					public void onTouchEnd(CapacitiveTouchTouchEndEvent e) {
						//System.out.println(e.toString());
						touchEndFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "capacitiveReleased" not defined
		}

		// capacitiveReleased(Channel)
		try {
			touchEndEventMethod =  PAppletParent.getClass().getMethod("capacitiveReleased", new Class<?>[] { Channel.class });
			if (touchEndEventMethod != null) {
				touchEndEventReportChannel = true;
				((CapacitiveTouch)device).addTouchEndListener(new CapacitiveTouchTouchEndListener() {
					public void onTouchEnd(CapacitiveTouchTouchEndEvent e) {
						//System.out.println(e.toString());
						touchEndFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "capacitiveReleased(channel)" not defined
		}

		// capacitiveReleasedRT()
		try {
			touchEndEventRTMethod =  PAppletParent.getClass().getMethod("capacitiveReleasedRT");
			if (touchEndEventRTMethod != null) {
				if (touchEndEventMethod != null) {
					System.err.println("Cannot use both capacitiveReleased() and capacitiveReleasedRT()."); 
				}
				else {			
					RTTouchEndEventRegister = true;
					touchEndEventReportChannel = false;
				}
			}
		} catch (Exception e) {
			// function "capacitiveReleasedRT" not defined
		}

		// capacitiveReleasedRT(Channel)
		try {
			touchEndEventRTMethod =  PAppletParent.getClass().getMethod("capacitiveReleasedRT", new Class<?>[] { Channel.class });
			if (touchEndEventRTMethod != null) {
				if (touchEndEventMethod != null) {
					System.err.println("Cannot use both capacitiveReleased() and capacitiveReleasedRT()."); 
				}
				else {			
					RTTouchEndEventRegister = true;
					touchEndEventReportChannel = true;
				}
			}
		} catch (Exception e) {
			// function "capacitiveReleasedRT" not defined
		}
	}

	@Override
	public void pre() {
		if (RTTouchEventRegister) {
			RTTouchEventRegister = false;
			try {
				if (touchEventReportChannel) { // capacitiveTouchedRT(Channel)
					((CapacitiveTouch)device).addTouchListener(new CapacitiveTouchTouchListener() {
						public void onTouch(CapacitiveTouchTouchEvent e) {
							//System.out.println(e.toString());
							try {
								if (touchEventRTMethod != null) {
									touchEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
								}
							} catch (Exception ex) {
								System.err.println("Disabling capacitiveTouchedRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								touchEventRTMethod = null;
							}
						}
					});
				} else { // capacitiveTouchedRT()
					((CapacitiveTouch)device).addTouchListener(new CapacitiveTouchTouchListener() {
						public void onTouch(CapacitiveTouchTouchEvent e) {
							//System.out.println(e.toString());
							try {
								if (touchEventRTMethod != null) {
									touchEventRTMethod.invoke(PAppletParent);
								}
							} catch (Exception ex) {
								System.err.println("Disabling capacitiveTouchedRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								touchEventRTMethod = null;
							}
						}
					});
				}
			} catch (Exception ex) {
		    	System.err.println("Disabling capacitiveTouchedRT() for " + deviceType + " because of an error:");
		    	ex.printStackTrace();
		    	touchEventRTMethod = null;
		    }
		}
		
		if (RTTouchEndEventRegister) {
			RTTouchEndEventRegister = false;
			try {
				if (touchEndEventReportChannel) { // capacitiveReleasedRT(Channel)
					((CapacitiveTouch)device).addTouchEndListener(new CapacitiveTouchTouchEndListener() {
						public void onTouchEnd(CapacitiveTouchTouchEndEvent e) {
							//System.out.println(e.toString());
							try {
								if (touchEndEventRTMethod != null) {
									touchEndEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
								}
							} catch (Exception ex) {
								System.err.println("Disabling capacitiveReleasedRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								touchEndEventRTMethod = null;
							}
						}
					});
				}
				else {  // capacitiveReleasedRT
					((CapacitiveTouch)device).addTouchEndListener(new CapacitiveTouchTouchEndListener() {
						public void onTouchEnd(CapacitiveTouchTouchEndEvent e) {
							//System.out.println(e.toString());
							try {
								if (touchEndEventRTMethod != null) {
									touchEndEventRTMethod.invoke(PAppletParent);
								}
							} catch (Exception ex) {
								System.err.println("Disabling capacitiveReleasedRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								touchEndEventRTMethod = null;
							}
						}
					});
				}
			} catch (Exception ex) {
		    	System.err.println("Disabling capacitiveReleasedRT() for " + deviceType + " because of an error:");
		    	ex.printStackTrace();
		    	touchEndEventRTMethod = null;
		    }
		}
	}
	
	@Override
	public void draw() {
		if (touchFlag) {
			touchFlag = false;
			try {
				if (touchEventMethod != null) {
					if (touchEventReportChannel) {
						touchEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						touchEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling capacitiveTouched() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				touchEventMethod = null;
			}
		}
	
		if (touchEndFlag) {
			touchEndFlag = false;
			try {
				if (touchEndEventMethod != null) {
					if (touchEndEventReportChannel) {
						touchEndEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						touchEndEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling capacitiveReleased() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				touchEndEventMethod = null;
			}
		}
	}

	@Override
	public int read() {
		try {
			if (!((CapacitiveTouch)device).getIsTouched()) return 0;
			double val =((CapacitiveTouch)device).getTouchValue();
			return (int)(val*1000);
		}
		catch (PhidgetException ex) {
			if (ex.getErrorCode() == com.phidget22.ErrorCode.UNKNOWN_VALUE) {
				return 0;
			}
			System.err.println("Cannot get value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0; 
	}

	@Override
	public int getDataInterval() {
		try {
			return ((CapacitiveTouch)device).getDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setDataInterval(int dataInterval) {
		try {
			((CapacitiveTouch)device).setDataInterval(dataInterval);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set data interval value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public int getMinDataInterval() {
		try {
			return ((CapacitiveTouch)device).getMinDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public int getMaxDataInterval() {
		try {
			return ((CapacitiveTouch)device).getMaxDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public boolean getIsTouched() {
		try {
			return ((CapacitiveTouch)device).getIsTouched();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get \"is touched\" value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return false;
	}

	@Override
	public float getSensitivity() {
		try {
			return (float)(((CapacitiveTouch)device).getSensitivity());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get sensitivity for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public void setSensitivity(float sensitivity) {
		try {
			((CapacitiveTouch)device).setSensitivity((double)sensitivity);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set sensitivity for device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getMinSensitivity() {
		try {
			return (float)(((CapacitiveTouch)device).getMinSensitivity());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min sensitivity for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public float getMaxSensitivity() {
		try {
			return (float)(((CapacitiveTouch)device).getMaxSensitivity());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max sensitivity for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public float getTouchValue() {
		try {
			if (!((CapacitiveTouch)device).getIsTouched()) return 0.0f;
			return (float)(((CapacitiveTouch)device).getTouchValue());
		}
		catch (PhidgetException ex) {
			if (ex.getErrorCode() == com.phidget22.ErrorCode.UNKNOWN_VALUE) {
				return 0.0f;
			}
			System.err.println("Cannot get touch value for device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0.0f;
	}

	@Override
	public float getMinTouchValue() {
		try {
			return (float)(((CapacitiveTouch)device).getMinTouchValue());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min touch value for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public float getMaxTouchValue() {
		try {
			return (float)(((CapacitiveTouch)device).getMaxTouchValue());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max touch value for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public float getTouchValueChangeTrigger() {
		try {
			return (float)(((CapacitiveTouch)device).getTouchValueChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get touch value change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public void setTouchValueChangeTrigger(float touchValueChangeTrigger) {
		try {
			((CapacitiveTouch)device).setTouchValueChangeTrigger((double)touchValueChangeTrigger);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set touch value change trigger for device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public void setReadChangeTrigger(int readChangeTrigger) {
		try {
			((CapacitiveTouch)device).setTouchValueChangeTrigger(((double)readChangeTrigger)/1000.0);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set read change trigger for device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getMinTouchValueChangeTrigger() {
		try {
			return (float)(((CapacitiveTouch)device).getMinTouchValueChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min touch value change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public float getMaxTouchValueChangeTrigger() {
		try {
			return (float)(((CapacitiveTouch)device).getMaxTouchValueChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max touch value change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}


}
