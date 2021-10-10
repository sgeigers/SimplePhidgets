package shenkar.SimplePhidgets;

import processing.core.*;
import java.lang.reflect.Method;
import com.phidget22.*;

public class P_Light_Sensor extends Device {
	// events
	Method sensorChangeEventMethod;  // sensorChange
	boolean sensorChangeFlag = false;
	boolean sensorChangeEventReportChannel = false;
	
	// real-time events
	Method sensorChangeEventRTMethod;  // sensorChangeRT
	boolean RTEventRegister = false;

	// max and min values - constant per device
	double maxIlluminance, minIlluminance;
	
	public P_Light_Sensor(PApplet P5Parent, Channel ChParent, String type, int serialNum, int portNum, int chNum) {
		super(P5Parent, ChParent, type, serialNum, portNum, chNum);

		// pre-opening setup
		try {
			device = new LightSensor();
		}	catch (PhidgetException ex) {
			System.err.println("Could not open device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
			PAppletParent.exit();
		}
				
		// device opening
		init(false);

		// post-opening setup
		try {
			// set maximum data rate as default
			((LightSensor)device).setDataInterval(((LightSensor)device).getMinDataInterval());
			
			// get min and max values for calculating read()
			minIlluminance = ((LightSensor)device).getMinIlluminance();
			maxIlluminance = ((LightSensor)device).getMaxIlluminance();
		}	catch (PhidgetException ex) {
			System.err.println("Could not set data interval for device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
		}
		attachListeners();
	}

	// check if "sensorChange()" or "sensorChangeRT()" were defined in the sketch and create a listener for it.
	void attachListeners() {
		// sensorChange()
		try {
			sensorChangeEventMethod =  PAppletParent.getClass().getMethod("sensorChange");
			if (sensorChangeEventMethod != null) {
				sensorChangeEventReportChannel = false;
				((LightSensor)device).addIlluminanceChangeListener(new LightSensorIlluminanceChangeListener() {
					public void onIlluminanceChange(LightSensorIlluminanceChangeEvent e) {
						//System.out.println(e.toString());
						sensorChangeFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "sensorChange" not defined
		}
		
		// sensorChange(Channel)
		try {
			sensorChangeEventMethod =  PAppletParent.getClass().getMethod("sensorChange", new Class<?>[] { Channel.class });
			if (sensorChangeEventMethod != null) {
				sensorChangeEventReportChannel = true;
				((LightSensor)device).addIlluminanceChangeListener(new LightSensorIlluminanceChangeListener() {
					public void onIlluminanceChange(LightSensorIlluminanceChangeEvent  e) {
						//System.out.println(e.toString());
						sensorChangeFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "sensorChange" not defined
		}
		
		// sensorChangeRT()
		try {
			sensorChangeEventRTMethod =  PAppletParent.getClass().getMethod("sensorChangeRT");
			if (sensorChangeEventRTMethod != null) {
				if (sensorChangeEventMethod != null) {
					System.err.println("Cannot use both sensorChange() and sensorChangeRT()."); 
				}
				else {
					RTEventRegister = true;
					sensorChangeEventReportChannel = false;
				}
			}
		} catch (Exception e) {
			// function "sensorChangeRT" not defined
		}

		// sensorChangeRT(Channel)
		try {
			sensorChangeEventRTMethod =  PAppletParent.getClass().getMethod("sensorChangeRT", new Class<?>[] { Channel.class });
			if (sensorChangeEventRTMethod != null) {
				if (sensorChangeEventMethod != null) {
					System.err.println("Cannot use both sensorChange() and sensorChangeRT()."); 
				}
				else {
					RTEventRegister = true;
					sensorChangeEventReportChannel = true;
				}
			}
		} catch (Exception e) {
			// function "sensorChangeRT" not defined
		}
	}

	@Override
	public void pre() {
		if (RTEventRegister) {
			RTEventRegister = false;
			try {
				if (sensorChangeEventReportChannel) { // sensorChangeRT(Channel)
					((LightSensor)device).addIlluminanceChangeListener(new LightSensorIlluminanceChangeListener() {
						public void onIlluminanceChange(LightSensorIlluminanceChangeEvent e) {
							//System.out.println(e.toString());
							try {
								if (sensorChangeEventRTMethod != null) {
									sensorChangeEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
								}
							} catch (Exception ex) {
								System.err.println("Disabling sensorChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								sensorChangeEventRTMethod = null;
							}
						}
					});
				}
				else { // sensorChangeRT()
					((LightSensor)device).addIlluminanceChangeListener(new LightSensorIlluminanceChangeListener() {
						public void onIlluminanceChange(LightSensorIlluminanceChangeEvent e) {
							//System.out.println(e.toString());
							try {
								if (sensorChangeEventRTMethod != null) {
									sensorChangeEventRTMethod.invoke(PAppletParent);
								}
							} catch (Exception ex) {
								System.err.println("Disabling sensorChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								sensorChangeEventRTMethod = null;
							}
						}
					});
				}
			} catch (Exception ex) {
		    	System.err.println("Disabling sensorChangeRT() for " + deviceType + " because of an error:");
		    	ex.printStackTrace();
		    	sensorChangeEventRTMethod = null;
		    }
		}
	}

	@Override
	public void draw() {
		if (sensorChangeFlag) {
			sensorChangeFlag = false;
			try {
				if (sensorChangeEventMethod != null) {
					if (sensorChangeEventReportChannel) {
						sensorChangeEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						sensorChangeEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling sensorChange() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				sensorChangeEventMethod = null;
			}
		}
	}

	@Override
	public int read() {
		try {
			double val =((LightSensor)device).getIlluminance();
			System.out.print(val + "/");
			val = (val-minIlluminance)/(maxIlluminance-minIlluminance);
			System.out.println(val + "//");
			return (int)(val*1000);
		}
		catch (PhidgetException ex) {
			if 	(ex.getErrorCode() == com.phidget22.ErrorCode.UNKNOWN_VALUE) { // catch the case of saturation and return max value
				return 1000;
			}
			System.err.println("Cannot get value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0; 
	}

	@Override
	public int getDataInterval() {
		try {
			return ((LightSensor)device).getDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setDataInterval(int dataInterval) {
		try {
			((LightSensor)device).setDataInterval(dataInterval);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set data interval value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public int getMinDataInterval() {
		try {
			return ((LightSensor)device).getMinDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public int getMaxDataInterval() {
		try {
			return ((LightSensor)device).getMaxDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public String getSensorUnit() {
		return "Lux";
	}

	@Override
	public float getSensorValue() {
		try {
			return (float)(((LightSensor)device).getIlluminance());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get sensor value from device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public boolean getSensorValueValidity() {
		try {
			@SuppressWarnings("unused")
			double reading = ((LightSensor)device).getIlluminance();
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
	public float getIlluminance() {
		try {
			return (float)(((LightSensor)device).getIlluminance());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get sensor value from device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public float getMinIlluminance() {
		try {
			return (float)(((LightSensor)device).getMinIlluminance());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get minimum illuminance level from device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}
	
	@Override
	public float getMaxIlluminance() {
		try {
			return (float)(((LightSensor)device).getMaxIlluminance());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get maximum illuminance level from device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}
	
	@Override
	public float getSensorValueChangeTrigger() {
		try {
			return (float)(((LightSensor)device).getIlluminanceChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get illuminance level from device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public void setSensorValueChangeTrigger(float sensorValueChangeTrigger) {
		try {
			((LightSensor)device).setIlluminanceChangeTrigger((double)sensorValueChangeTrigger);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set sensor value change trigger for device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getIlluminanceChangeTrigger() {
		try {
			return (float)(((LightSensor)device).getIlluminanceChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get illuminance level from device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public void setIlluminanceChangeTrigger(float sensorValueChangeTrigger) {
		try {
			((LightSensor)device).setIlluminanceChangeTrigger((double)sensorValueChangeTrigger);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set sensor value change trigger for device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getMinIlluminanceChangeTrigger() {
		try {
			return (float)(((LightSensor)device).getMinIlluminanceChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get minimum illuminance level from device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public float getMaxIlluminanceChangeTrigger() {
		try {
			return (float)(((LightSensor)device).getMaxIlluminanceChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get maximum illuminance level from device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}
}
