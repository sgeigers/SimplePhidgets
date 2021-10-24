package shenkar.SimplePhidgets;

import processing.core.*;
import java.lang.reflect.Method;
import com.phidget22.*;

public class P_GPS extends Device {
	// event
	Method positionChangeEventMethod;  // positionChange
	Method headingChangeEventMethod;  // headingChange
	Method positionFixStateEventMethod;  // positionFixState
	boolean positionChangeFlag = false;
	boolean headingChangeFlag = false;
	boolean positionFixStateFlag = false;
	boolean positionChangeEventReportChannel = false;
	boolean headingChangeEventReportChannel = false;
	boolean positionFixStateEventReportChannel = false;

	// real-time event
	Method positionChangeEventRTMethod;  // positionChangeRT
	Method headingChangeEventRTMethod;  // headingChangeRT
	Method positionFixStateEventRTMethod;  // positionFixStateRT
	boolean RTpositionChangeEventRegister = false;
	boolean RTheadingChangeEventRegister = false;
	boolean RTpositionFixStateEventRegister = false;

	public P_GPS(PApplet P5Parent, Channel ChParent, String type, int serialNum, int portNum, int chNum) {
		super(P5Parent, ChParent, type, serialNum, portNum, chNum);

		// pre-opening setup
		try {
			device = new GPS();
		}	catch (PhidgetException ex) {
			System.err.println("Could not open device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
			PAppletParent.exit();
		}

		// device opening
		init(false);

		// post-opening setup
		attachListeners();
	}

	// check if "positionChange()", "headingChange()" or "positionFixState()" were defined in the sketch and create a listener for them.
	void attachListeners() {
		// positionChange()
		try {
			positionChangeEventMethod =  PAppletParent.getClass().getMethod("positionChange");
			if (positionChangeEventMethod != null) {
				positionChangeEventReportChannel = false;
				((GPS)device).addPositionChangeListener(new GPSPositionChangeListener() {
					public void onPositionChange(GPSPositionChangeEvent e) {
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
				((GPS)device).addPositionChangeListener(new GPSPositionChangeListener() {
					public void onPositionChange(GPSPositionChangeEvent e) {
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
					RTpositionChangeEventRegister = true;
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
					RTpositionChangeEventRegister = true;
					positionChangeEventReportChannel = true;
				}
			}
		} catch (Exception e) {
			// function "positionChangeRT(Channel)" not defined
		}

		// headingChange()
		try {
			headingChangeEventMethod =  PAppletParent.getClass().getMethod("headingChange");
			if (headingChangeEventMethod != null) {
				headingChangeEventReportChannel = false;
				((GPS)device).addHeadingChangeListener(new GPSHeadingChangeListener() {
					public void onHeadingChange(GPSHeadingChangeEvent e) {
						//System.out.println(e.toString());
						positionChangeFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "positionChange" not defined
		}

		// headingChange(Channel)
		try {
			headingChangeEventMethod =  PAppletParent.getClass().getMethod("headingChange", new Class<?>[] { Channel.class });
			if (headingChangeEventMethod != null) {
				headingChangeEventReportChannel = true;
				((GPS)device).addHeadingChangeListener(new GPSHeadingChangeListener() {
					public void onHeadingChange(GPSHeadingChangeEvent e) {
						//System.out.println(e.toString());
						positionChangeFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "positionChange(Channel)" not defined
		}

		// headingChangeRT()
		try {
			headingChangeEventRTMethod =  PAppletParent.getClass().getMethod("headingChangeRT");
			if (headingChangeEventRTMethod != null) {
				if (headingChangeEventMethod != null) {
					System.err.println("Cannot use both headingChange() and headingChangeRT()."); 
				}
				else {
					RTheadingChangeEventRegister = true;
					headingChangeEventReportChannel = false;
				}
			}
		} catch (Exception e) {
			// function "positionChangeRT()" not defined
		}

		// headingChangeRT(Channel)
		try {
			headingChangeEventRTMethod =  PAppletParent.getClass().getMethod("headingChangeRT", new Class<?>[] { Channel.class });
			if (headingChangeEventRTMethod != null) {
				if (headingChangeEventMethod != null) {
					System.err.println("Cannot use both headingChange() and headingChangeRT()."); 
				}
				else {
					RTheadingChangeEventRegister = true;
					headingChangeEventReportChannel = true;
				}
			}
		} catch (Exception e) {
			// function "headingChangeRT(Channel)" not defined
		}

		// positionFixState()
		try {
			positionFixStateEventMethod =  PAppletParent.getClass().getMethod("positionFixState");
			if (positionFixStateEventMethod != null) {
				positionFixStateEventReportChannel = false;
				((GPS)device).addPositionFixStateChangeListener(new GPSPositionFixStateChangeListener() {
					public void onPositionFixStateChange(GPSPositionFixStateChangeEvent e) {
						//System.out.println(e.toString());
						positionFixStateFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "positionFixState" not defined
		}

		// positionFixState(Channel)
		try {
			positionFixStateEventMethod =  PAppletParent.getClass().getMethod("positionFixState", new Class<?>[] { Channel.class });
			if (positionFixStateEventMethod != null) {
				positionFixStateEventReportChannel = true;
				((GPS)device).addPositionFixStateChangeListener(new GPSPositionFixStateChangeListener() {
					public void onPositionFixStateChange(GPSPositionFixStateChangeEvent e) {
						//System.out.println(e.toString());
						positionFixStateFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "positionFixState(Channel)" not defined
		}

		// positionFixStateRT()
		try {
			positionFixStateEventRTMethod =  PAppletParent.getClass().getMethod("positionFixStateRT");
			if (positionFixStateEventRTMethod != null) {
				if (positionFixStateEventMethod != null) {
					System.err.println("Cannot use both positionFixState() and positionFixStateRT()."); 
				}
				else {
					RTpositionFixStateEventRegister = true;
					positionFixStateEventReportChannel = false;
				}
			}
		} catch (Exception e) {
			// function "positionFixStateRT()" not defined
		}

		// positionFixStateRT(Channel)
		try {
			headingChangeEventRTMethod =  PAppletParent.getClass().getMethod("positionFixStateRT", new Class<?>[] { Channel.class });
			if (headingChangeEventRTMethod != null) {
				if (positionFixStateEventMethod != null) {
					System.err.println("Cannot use both positionFixState() and positionFixStateRT()."); 
				}
				else {
					RTpositionFixStateEventRegister = true;
					positionFixStateEventReportChannel = true;
				}
			}
		} catch (Exception e) {
			// function "positionFixStateRT(Channel)" not defined
		}
	}

	public void pre() {
		// positionChangeRT
		if (RTpositionChangeEventRegister) {
			RTpositionChangeEventRegister = false;
			try {  
				if (positionChangeEventReportChannel) { // positionChangeRT(Channel)
					((GPS)device).addPositionChangeListener(new GPSPositionChangeListener() {
						public void onPositionChange(GPSPositionChangeEvent e) {
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
					((GPS)device).addPositionChangeListener(new GPSPositionChangeListener() {
						public void onPositionChange(GPSPositionChangeEvent e) {
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
		
		// headingChangeRT
		if (RTheadingChangeEventRegister) {
			RTheadingChangeEventRegister = false;
			try {  
				if (headingChangeEventReportChannel) { // headingChangeRT(Channel)
					((GPS)device).addHeadingChangeListener(new GPSHeadingChangeListener() {
						public void onHeadingChange(GPSHeadingChangeEvent e) {
							//System.out.println(e.toString());
							try {
								if (headingChangeEventRTMethod != null) {
									headingChangeEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
								}
							} catch (Exception ex) {
								System.err.println("Disabling headingChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								headingChangeEventRTMethod = null;
							}
						}
					});
				}
				else { // headingChangeRT()
					((GPS)device).addHeadingChangeListener(new GPSHeadingChangeListener() {
						public void onHeadingChange(GPSHeadingChangeEvent e) {
							//System.out.println(e.toString());
							try {
								if (headingChangeEventRTMethod != null) {
									headingChangeEventRTMethod.invoke(PAppletParent);
								}
							} catch (Exception ex) {
								System.err.println("Disabling headingChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								headingChangeEventRTMethod = null;
							}
						}
					});
				}
			} catch (Exception ex) {
		    	System.err.println("Disabling headingChangeRT() for " + deviceType + " because of an error:");
		    	ex.printStackTrace();
		    	headingChangeEventRTMethod = null;
		    }
		}
		
		// positionFixStateRT
		if (RTpositionFixStateEventRegister) {
			RTpositionFixStateEventRegister = false;
			try {  
				if (positionFixStateEventReportChannel) { // positionFixStateRT(Channel)
					((GPS)device).addPositionFixStateChangeListener(new GPSPositionFixStateChangeListener() {
						public void onPositionFixStateChange(GPSPositionFixStateChangeEvent e) {
							//System.out.println(e.toString());
							try {
								if (positionFixStateEventRTMethod != null) {
									positionFixStateEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
								}
							} catch (Exception ex) {
								System.err.println("Disabling positionFixStateRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								positionFixStateEventRTMethod = null;
							}
						}
					});
				}
				else { // positionFixStateRT()
					((GPS)device).addPositionFixStateChangeListener(new GPSPositionFixStateChangeListener() {
						public void onPositionFixStateChange(GPSPositionFixStateChangeEvent e) {
							//System.out.println(e.toString());
							try {
								if (positionFixStateEventRTMethod != null) {
									positionFixStateEventRTMethod.invoke(PAppletParent);
								}
							} catch (Exception ex) {
								System.err.println("Disabling positionFixStateRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								positionFixStateEventRTMethod = null;
							}
						}
					});
				}
			} catch (Exception ex) {
		    	System.err.println("Disabling positionFixStateRT() for " + deviceType + " because of an error:");
		    	ex.printStackTrace();
		    	positionFixStateEventRTMethod = null;
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

		if (headingChangeFlag) {
			headingChangeFlag = false;
			try {
				if (headingChangeEventMethod != null) {
					if (headingChangeEventReportChannel) {
						headingChangeEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						headingChangeEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling headingChange() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				headingChangeEventMethod = null;
			}
		}
		
		if (positionFixStateFlag) {
			positionFixStateFlag = false;
			try {
				if (positionFixStateEventMethod != null) {
					if (positionFixStateEventReportChannel) {
						positionFixStateEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						positionFixStateEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling positionFixState() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				positionFixStateEventMethod = null;
			}
		}
	}

	@Override
	public float getAltitude() {
		try {
			return (float)(((GPS)device).getAltitude());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get altitude value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0;
	}

	@Override
	public int getDay() {
		try {
			return (int)(((GPS)device).getDate().tm_mday);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get day value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0;
	}

	@Override
	public int getMonth() {
		try {
			return (int)(((GPS)device).getDate().tm_mon);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get month value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0;
	}

	@Override
	public int getYear() {
		try {
			return (int)(((GPS)device).getDate().tm_year);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get year value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0;
	}

	@Override
	public java.util.Calendar getDateAndTime() {
		try {
			return ((GPS)device).getDateAndTime();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get date and time (java.util.Calendar) from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return java.util.Calendar.getInstance();
	}

	@Override
	public float getHeading() {
		try {
			return (float)(((GPS)device).getHeading());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get heading value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0;
	}

	@Override
	public float getLatitude() {
		try {
			return (float)(((GPS)device).getLatitude());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get latitude value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0;
	}

	@Override
	public float getLongitude() {
		try {
			return (float)(((GPS)device).getLongitude());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get longitude value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0;
	}

	
	@Override
	public boolean getPositionFixState() {
		try {
			return ((GPS)device).getPositionFixState();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get position fix state from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return false;
	}

	@Override
	public int getMilliseconds() {
		try {
			return (int)(((GPS)device).getTime().tm_ms);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get milliseconds value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0;
	}

	@Override
	public int getSeconds() {
		try {
			return (int)(((GPS)device).getTime().tm_sec);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get seconds value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0;
	}

	@Override
	public int getMinutes() {
		try {
			return (int)(((GPS)device).getTime().tm_min);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get minutes value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0;
	}

	@Override
	public int getHours() {
		try {
			return (int)(((GPS)device).getTime().tm_hour);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get hours value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0;
	}

	@Override
	public float getVelocity() {
		try {
			return (float)(((GPS)device).getVelocity());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get velocity value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0;
	}
	
	@Override
	public NMEAData getNMEAData() {
		try {
			return ((GPS)device).getNMEAData();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get velocity value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return new NMEAData();
	}
}
