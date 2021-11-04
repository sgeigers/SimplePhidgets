package shenkar.SimplePhidgets;

import processing.core.*;
import java.lang.reflect.Method;
import com.phidget22.*;

public class P_Distance_Sensor extends Device {
	// events
	Method distanceChangeEventMethod;  // distanceChange
	Method sonarReflectionsChangeEventMethod; // sonarReflectionsChange
	boolean distanceChangeFlag = false;
	boolean sonarReflectionsChangeFlag = false;
	boolean distanceChangeEventReportChannel = false;
	boolean sonarReflectionsChangeEventReportChannel = false;

	// real-time events
	Method distanceChangeEventRTMethod;  // distanceChangeRT
	Method sonarReflectionsChangeEventRTMethod; // sonarReflectionsChangeRT
	boolean RTDistanceChangeEventRegister = false;
	boolean RTSonarReflectionsChangeEventRegister = false;
	
	// variables for calculating read() to be 0..1000
	int minDist;
	int maxDist;
		
	public P_Distance_Sensor(PApplet P5Parent, Channel ChParent, String type, int serialNum, int portNum, int chNum) {
		super(P5Parent, ChParent, type, serialNum, portNum, chNum);

		// pre-opening setup
		try {
			device = new DistanceSensor();
		}	catch (PhidgetException ex) {
			System.err.println("Could not open device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
			PAppletParent.exit();
		}
				
		// device opening
		init(false);

		// post-opening setup
		try {
			// set maximum data rate as default
			((DistanceSensor)device).setDataInterval(((DistanceSensor)device).getMinDataInterval());
		}	catch (PhidgetException ex) {
			System.err.println("Could not set data interval for device " + deviceType + " on port " + portNum + " because of error:" + ex.toString());
		}
		// variables for calculating read() to be 0..1000
		try {
			minDist = ((DistanceSensor)device).getMinDistance();
			maxDist = ((DistanceSensor)device).getMaxDistance();
		}	catch (PhidgetException ex) {
			System.err.println("Could not get minimum and maximum distances for device " + deviceType + " on port " + portNum + " because of error:" + ex.toString());
		}
		attachListeners();
	}

	// check if "distanceChange()" or "sonarReflectionsChange()" were defined in the sketch and create listeners for them.
	void attachListeners() {
		
		// distanceChange()
		try {
			distanceChangeEventMethod =  PAppletParent.getClass().getMethod("distanceChange");
			if (distanceChangeEventMethod != null) {
				distanceChangeEventReportChannel = false;
				((DistanceSensor)device).addDistanceChangeListener(new DistanceSensorDistanceChangeListener() {
					public void onDistanceChange(DistanceSensorDistanceChangeEvent e) {
						//System.out.println(e.toString());
						distanceChangeFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "distanceChange" not defined
		}

		// distanceChange(Channel)
		try {
			distanceChangeEventMethod =  PAppletParent.getClass().getMethod("distanceChange", new Class<?>[] { Channel.class });
			if (distanceChangeEventMethod != null) {
				distanceChangeEventReportChannel = true;
				((DistanceSensor)device).addDistanceChangeListener(new DistanceSensorDistanceChangeListener() {
					public void onDistanceChange(DistanceSensorDistanceChangeEvent e) {
						//System.out.println(e.toString());
						distanceChangeFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "distanceChange" not defined
		}

		// distanceChangeRT()
		try {
			distanceChangeEventRTMethod =  PAppletParent.getClass().getMethod("distanceChangeRT");
			if (distanceChangeEventRTMethod != null) {
				if (distanceChangeEventMethod != null) {
					System.err.println("Cannot use both distanceChange() and distanceChangeRT()."); 
				}
				else {			
					RTDistanceChangeEventRegister = true;
					distanceChangeEventReportChannel = false;
				}
			}
		} catch (Exception e) {
			// function "distanceChangeRT" not defined
		}

		// distanceChangeRT(Channel)
		try {
			distanceChangeEventRTMethod =  PAppletParent.getClass().getMethod("distanceChangeRT", new Class<?>[] { Channel.class });
			if (distanceChangeEventRTMethod != null) {
				if (distanceChangeEventMethod != null) {
					System.err.println("Cannot use both distanceChange() and distanceChangeRT()."); 
				}
				else {			
					RTDistanceChangeEventRegister = true;
					distanceChangeEventReportChannel = true;
				}
			}
		} catch (Exception e) {
			// function "distanceChangeRT(channel)" not defined
		}

		// sonarReflectionsChange()
		try {
			sonarReflectionsChangeEventMethod =  PAppletParent.getClass().getMethod("sonarReflectionsChange");
			if (sonarReflectionsChangeEventMethod != null) {
				sonarReflectionsChangeEventReportChannel = false;
				((DistanceSensor)device).addSonarReflectionsUpdateListener(new DistanceSensorSonarReflectionsUpdateListener() {
					public void onSonarReflectionsUpdate(DistanceSensorSonarReflectionsUpdateEvent e) {
						//System.out.println(e.toString());
						sonarReflectionsChangeFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "sonarReflectionsChange" not defined
		}

		// sonarReflectionsChange(Channel)
		try {
			sonarReflectionsChangeEventMethod =  PAppletParent.getClass().getMethod("sonarReflectionsChange", new Class<?>[] { Channel.class });
			if (sonarReflectionsChangeEventMethod != null) {
				sonarReflectionsChangeEventReportChannel = true;
				((DistanceSensor)device).addSonarReflectionsUpdateListener(new DistanceSensorSonarReflectionsUpdateListener() {
					public void onSonarReflectionsUpdate(DistanceSensorSonarReflectionsUpdateEvent e) {
						//System.out.println(e.toString());
						sonarReflectionsChangeFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "sonarReflectionsChange(channel)" not defined
		}

		// sonarReflectionsChangeRT()
		try {
			sonarReflectionsChangeEventRTMethod =  PAppletParent.getClass().getMethod("sonarReflectionsChangeRT");
			if (sonarReflectionsChangeEventRTMethod != null) {
				if (sonarReflectionsChangeEventMethod != null) {
					System.err.println("Cannot use both sonarReflectionsChange() and sonarReflectionsChangeRT()."); 
				}
				else {			
					RTSonarReflectionsChangeEventRegister = true;
					sonarReflectionsChangeEventReportChannel = false;
				}
			}
		} catch (Exception e) {
			// function "sonarReflectionsChangeRT" not defined
		}

		// sonarReflectionsChangeRT(Channel)
		try {
			sonarReflectionsChangeEventRTMethod =  PAppletParent.getClass().getMethod("sonarReflectionsChangeRT", new Class<?>[] { Channel.class });
			if (sonarReflectionsChangeEventRTMethod != null) {
				if (sonarReflectionsChangeEventMethod != null) {
					System.err.println("Cannot use both sonarReflectionsChange() and sonarReflectionsChangeRT()."); 
				}
				else {			
					RTSonarReflectionsChangeEventRegister = true;
					sonarReflectionsChangeEventReportChannel = true;
				}
			}
		} catch (Exception e) {
			// function "sonarReflectionsChangeRT" not defined
		}
	}

	@Override
	public void pre() {
		if (RTDistanceChangeEventRegister) {
			RTDistanceChangeEventRegister = false;
			try {
				if (distanceChangeEventReportChannel) { // distanceChangeRT(Channel)
					((DistanceSensor)device).addDistanceChangeListener(new DistanceSensorDistanceChangeListener() {
						public void onDistanceChange(DistanceSensorDistanceChangeEvent e) {
							//System.out.println(e.toString());
							try {
								if (distanceChangeEventRTMethod != null) {
									distanceChangeEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
								}
							} catch (Exception ex) {
								System.err.println("Disabling distanceChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								distanceChangeEventRTMethod = null;
							}
						}
					});
				} else { // distanceChangeRT()
					((DistanceSensor)device).addDistanceChangeListener(new DistanceSensorDistanceChangeListener() {
						public void onDistanceChange(DistanceSensorDistanceChangeEvent e) {
							//System.out.println(e.toString());
							try {
								if (distanceChangeEventRTMethod != null) {
									distanceChangeEventRTMethod.invoke(PAppletParent);
								}
							} catch (Exception ex) {
								System.err.println("Disabling distanceChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								distanceChangeEventRTMethod = null;
							}
						}
					});
				}
			} catch (Exception ex) {
		    	System.err.println("Disabling distanceChangeRT() for " + deviceType + " because of an error:");
		    	ex.printStackTrace();
		    	distanceChangeEventRTMethod = null;
		    }
		}
		
		if (RTSonarReflectionsChangeEventRegister) {
			RTSonarReflectionsChangeEventRegister = false;
			try {
				if (sonarReflectionsChangeEventReportChannel) { // sonarReflectionsChangeRT(Channel)
					((DistanceSensor)device).addSonarReflectionsUpdateListener(new DistanceSensorSonarReflectionsUpdateListener() {
						public void onSonarReflectionsUpdate(DistanceSensorSonarReflectionsUpdateEvent e) {
							//System.out.println(e.toString());
							try {
								if (sonarReflectionsChangeEventRTMethod != null) {
									sonarReflectionsChangeEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
								}
							} catch (Exception ex) {
								System.err.println("Disabling sonarReflectionsChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								sonarReflectionsChangeEventRTMethod = null;
							}
						}
					});
				}
				else {  // sonarReflectionsChangeRT
					((DistanceSensor)device).addSonarReflectionsUpdateListener(new DistanceSensorSonarReflectionsUpdateListener() {
						public void onSonarReflectionsUpdate(DistanceSensorSonarReflectionsUpdateEvent e) {
							//System.out.println(e.toString());
							try {
								if (sonarReflectionsChangeEventRTMethod != null) {
									sonarReflectionsChangeEventRTMethod.invoke(PAppletParent);
								}
							} catch (Exception ex) {
								System.err.println("Disabling sonarReflectionsChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								sonarReflectionsChangeEventRTMethod = null;
							}
						}
					});
				}
			} catch (Exception ex) {
		    	System.err.println("Disabling sonarReflectionsChangeRT() for " + deviceType + " because of an error:");
		    	ex.printStackTrace();
		    	sonarReflectionsChangeEventRTMethod = null;
		    }
		}
	}
	
	@Override
	public void draw() {
		if (distanceChangeFlag) {
			distanceChangeFlag = false;
			try {
				if (distanceChangeEventMethod != null) {
					if (distanceChangeEventReportChannel) {
						distanceChangeEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						distanceChangeEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling distanceChange() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				distanceChangeEventMethod = null;
			}
		}
	
		if (sonarReflectionsChangeFlag) {
			sonarReflectionsChangeFlag = false;
			try {
				if (sonarReflectionsChangeEventMethod != null) {
					if (sonarReflectionsChangeEventReportChannel) {
						sonarReflectionsChangeEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						sonarReflectionsChangeEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling sonarReflectionsChange() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				sonarReflectionsChangeEventMethod = null;
			}
		}
	}

	/*
	 * most basic way to use the channel. Returns distance as an int between 0 and 1000 (arbitrary units)
	 * 
	 */
	@Override
	public int read() {
		try {
			int dist = ((DistanceSensor)device).getDistance();
			dist = ((dist - minDist) * 1000) / (maxDist - minDist);
			return dist;
		}
		catch (PhidgetException ex) {
			if (ex.getErrorCode() == com.phidget22.ErrorCode.UNKNOWN_VALUE) {
				return 0;
			}
			System.err.println("Cannot get read value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0; 
	}

	@Override
	public float getSensorValue() {
		try {
			return (float)(((DistanceSensor)device).getDistance());
		}
		catch (PhidgetException ex) {
			if (ex.getErrorCode() == com.phidget22.ErrorCode.UNKNOWN_VALUE) {
				return 0;
			}
			System.err.println("Cannot get sensor value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0; 
	}

	@Override
	public boolean getSensorValueValidity() {
		try {
			@SuppressWarnings("unused")
			int reading = ((DistanceSensor)device).getDistance();
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
	public String getSensorUnit() {
		return "millimiters";
	}

	@Override
	public int getDataInterval() {
		try {
			return ((DistanceSensor)device).getDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setDataInterval(int dataInterval) {
		try {
			((DistanceSensor)device).setDataInterval(dataInterval);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set data interval value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public int getMinDataInterval() {
		try {
			return ((DistanceSensor)device).getMinDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public int getMaxDataInterval() {
		try {
			return ((DistanceSensor)device).getMaxDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public int getDistance() {
		try {
			return ((DistanceSensor)device).getDistance();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get distance value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0;
	}

	@Override
	public int getMinDistance() {
		try {
			return ((DistanceSensor)device).getMinDistance();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get minimum distance value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0;
	}

	@Override
	public int getMaxDistance() {
		try {
			return ((DistanceSensor)device).getMaxDistance();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get maximum distance value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0;
	}

	@Override
	public int getDistanceChangeTrigger() {
		try {
			return ((DistanceSensor)device).getDistanceChangeTrigger();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get distance change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setDistanceChangeTrigger(int trigger) {
		try {
			((DistanceSensor)device).setDistanceChangeTrigger(trigger);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set distance change trigger for device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public int getMinDistanceChangeTrigger() {
		try {
			return ((DistanceSensor)device).getMinDistanceChangeTrigger();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get minimum distance change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public int getMaxDistanceChangeTrigger() {
		try {
			return ((DistanceSensor)device).getMaxDistanceChangeTrigger();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get maximum distance change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setSonarQuietMode(boolean mode) {
		if (deviceType.equals("DST1200")) {
			try {
				((DistanceSensor)device).setSonarQuietMode(mode);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set sonar quiet mode for device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("setSonarQuietMode(boolean) is not valid for device of type " + deviceType);	
			PAppletParent.exit();
		}
	}

	@Override
	public boolean getSonarQuietMode() {
		if (deviceType.equals("DST1200")) {
			try {
				return ((DistanceSensor)device).getSonarQuietMode();
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get sonar quiet mode from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getSonarQuietMode() is not valid for device of type " + deviceType);	
			PAppletParent.exit();
		}
		return false;
	}

	@Override
	public DistanceSensorSonarReflections getSonarReflections() {
		if (deviceType.equals("DST1200")) {
			try {
				return ((DistanceSensor)device).getSonarReflections();
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get solar reflections from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getSonarReflections() is not valid for device of type " + deviceType);	
			PAppletParent.exit();
		}
		return new DistanceSensorSonarReflections();
	}
}
