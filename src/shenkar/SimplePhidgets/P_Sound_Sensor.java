package shenkar.SimplePhidgets;

import processing.core.*;
import java.lang.reflect.Method;
import com.phidget22.*;

public class P_Sound_Sensor extends Device {
	// events
	Method SoundSensorSPLChangeEventMethod;  // sensorChange
	boolean soundChangeFlag = false;
	boolean SoundSensorSPLChangeEventReportChannel = false;
	
	// real-time event
	Method SoundSensorSPLChangeEventRTMethod;  // sensorChangeRT
	boolean RTEventRegister = false;

	public P_Sound_Sensor(PApplet P5Parent, Channel ChParent, String type, int serialNum, int portNum, int chNum) {
		super(P5Parent, ChParent, type, serialNum, portNum, chNum);

		// pre-opening setup
		try {
			device = new SoundSensor();
		}	catch (PhidgetException ex) {
			System.err.println("Could not open device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
			PAppletParent.exit();
		}
				
		// device opening
		init(false);

		// post-opening setup
		try {
			// set maximum data rate as default
			((SoundSensor)device).setDataInterval(((SoundSensor)device).getMinDataInterval());
		}
		catch (PhidgetException ex) {
			System.err.println("Could not set data interval for device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
		}
		attachListeners();
	}

	// check if "soundChange()" was defined in the sketch and create a listener for it.
	void attachListeners() {
		// sensorChange()
		try {
			SoundSensorSPLChangeEventMethod =  PAppletParent.getClass().getMethod("sensorChange");
			if (SoundSensorSPLChangeEventMethod != null) {
				SoundSensorSPLChangeEventReportChannel = false;
				((SoundSensor)device).addSPLChangeListener(new SoundSensorSPLChangeListener() {
					public void onSPLChange(SoundSensorSPLChangeEvent  e) {
						//System.out.println(e.toString());
						soundChangeFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "sensorChange()" not defined
		}

		// sensorChange(Channel)
		try {
			SoundSensorSPLChangeEventMethod =  PAppletParent.getClass().getMethod("sensorChange", new Class<?>[] { Channel.class });
			if (SoundSensorSPLChangeEventMethod != null) {
				SoundSensorSPLChangeEventReportChannel = true;
				((SoundSensor)device).addSPLChangeListener(new SoundSensorSPLChangeListener() {
					public void onSPLChange(SoundSensorSPLChangeEvent  e) {
						//System.out.println(e.toString());
						soundChangeFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "sensorChange(Channel)" not defined
		}

		// sensorChangeRT()
		try {
			SoundSensorSPLChangeEventRTMethod =  PAppletParent.getClass().getMethod("soundChangeRT");
			if (SoundSensorSPLChangeEventRTMethod != null) {
				if (SoundSensorSPLChangeEventMethod != null) {
					System.err.println("Cannot use both sensorChange() and sensorChangeRT()."); 
				}
				else {
					RTEventRegister = true;
					SoundSensorSPLChangeEventReportChannel = false;
				}
			}
		} catch (Exception e) {
			// function "sensorChangeRT()" not defined
		}

		// sensorChangeRT(Channel)
		try {
			SoundSensorSPLChangeEventRTMethod =  PAppletParent.getClass().getMethod("soundChangeRT", new Class<?>[] { Channel.class });
			if (SoundSensorSPLChangeEventRTMethod != null) {
				if (SoundSensorSPLChangeEventMethod != null) {
					System.err.println("Cannot use both sensorChange() and sensorChangeRT()."); 
				}
				else {
					RTEventRegister = true;
					SoundSensorSPLChangeEventReportChannel = true;
				}
			}
		} catch (Exception e) {
			// function "sensorChangeRT(Channel)" not defined
		}
	}

	@Override
	public void pre() {
		if (RTEventRegister) {
			RTEventRegister = false;
			try {
				if (SoundSensorSPLChangeEventReportChannel) { // sensorChangeRT(Channel)
					((SoundSensor)device).addSPLChangeListener(new SoundSensorSPLChangeListener() {
						public void onSPLChange(SoundSensorSPLChangeEvent  e) {
							//System.out.println(e.toString());
							try {
								if (SoundSensorSPLChangeEventRTMethod != null) {
									SoundSensorSPLChangeEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
								}
							} catch (Exception ex) {
								System.err.println("Disabling soundChangeRT(Channel) for " + deviceType + " because of an error:");
								ex.printStackTrace();
								SoundSensorSPLChangeEventRTMethod = null;
							}
						}
					});
				}
				else { // sensorChangeRT()
					((SoundSensor)device).addSPLChangeListener(new SoundSensorSPLChangeListener() {
						public void onSPLChange(SoundSensorSPLChangeEvent  e) {
							//System.out.println(e.toString());
							try {
								if (SoundSensorSPLChangeEventRTMethod != null) {
									SoundSensorSPLChangeEventRTMethod.invoke(PAppletParent);
								}
							} catch (Exception ex) {
								System.err.println("Disabling soundChangeRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								SoundSensorSPLChangeEventRTMethod = null;
							}
						}
					});
				}
			} catch (Exception ex) {
		    	System.err.println("Disabling sensorChangeRT() for " + deviceType + " because of an error:");
		    	ex.printStackTrace();
		    	SoundSensorSPLChangeEventRTMethod = null;
		    }
		}
	}

	@Override
	public void draw() {
		if (soundChangeFlag) {
			soundChangeFlag = false;
			try {
				if (SoundSensorSPLChangeEventMethod != null) {
					if (SoundSensorSPLChangeEventReportChannel) {
						SoundSensorSPLChangeEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						SoundSensorSPLChangeEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling sensorChange() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				SoundSensorSPLChangeEventMethod = null;
			}
		}
	}

	@Override
	public int read() {
		try {
			double val =((SoundSensor)device).getdBA();
			val /= ((SoundSensor)device).getMaxdB();
			return (int)(val*1000);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0; 
	}

	@Override
	public int getDataInterval() {
		try {
			return ((SoundSensor)device).getDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setDataInterval(int dataInterval) {
		try {
			((SoundSensor)device).setDataInterval(dataInterval);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set data interval value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public int getMinDataInterval() {
		try {
			return ((SoundSensor)device).getMinDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public int getMaxDataInterval() {
		try {
			return ((SoundSensor)device).getMaxDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public float getdB() {
		try {
			return (float)(((SoundSensor)device).getdB());
		}
		catch (PhidgetException ex) {
			if 	(ex.getErrorCode() == com.phidget22.ErrorCode.UNKNOWN_VALUE) {
				System.err.println("Sound level (dB)  out of range");
			}
			else {
				System.err.println("Cannot get sound level (dB) for device " + deviceType + " because of error: " + ex);
				PAppletParent.exit();
			}
		}
		return 0.0f;
	}

	@Override
	public float getMaxdB() {
		try {
			return (float)(((SoundSensor)device).getMaxdB());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max sound level for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public float getdBA() {
		try {
			return (float)(((SoundSensor)device).getdBA());
		}
		catch (PhidgetException ex) {
			if 	(ex.getErrorCode() == com.phidget22.ErrorCode.UNKNOWN_VALUE) {
				System.err.println("Sound level (dBA) out of range");
			}
			else {
				System.err.println("Cannot get sound level (dBA) for device " + deviceType + " because of error: " + ex);
				PAppletParent.exit();
			}
		}
		return 0.0f;
	}

	@Override
	public float getdBC() {
		try {
			return (float)(((SoundSensor)device).getdBC());
		}
		catch (PhidgetException ex) {
			if 	(ex.getErrorCode() == com.phidget22.ErrorCode.UNKNOWN_VALUE) {
				System.err.println("Sound level (dBC)  out of range");
			}
			else {
				System.err.println("Cannot get sound level (dBC) for device " + deviceType + " because of error: " + ex);
				PAppletParent.exit();
			}
		}
		return 0.0f;
	}

	@Override
	public float getNoiseFloor() {
		try {
			return (float)(((SoundSensor)device).getNoiseFloor());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get noise floor for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public float[] getOctaves() {
		try {
			double[] dOct = ((SoundSensor)device).getOctaves();
			float[] fOct = new float[10];
			for (int i=0; i<10; i++) {
				fOct[i] = (float)dOct[i];
			}
			return fOct;
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get octaves data for device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return null;
	}

	@Override
	public float getSPLChangeTrigger() {
		try {
			return (float)(((SoundSensor)device).getSPLChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get sound pressure level (SPL) change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public void setSPLChangeTrigger(float SPLChangeTrigger) {
		try {
			((SoundSensor)device).setSPLChangeTrigger((double)SPLChangeTrigger);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set sound pressure level (SPL) change trigger for device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public void setReadChangeTrigger(int readChangeTrigger) {
		try {
			((SoundSensor)device).setSPLChangeTrigger(((double)readChangeTrigger)/1000.0*((SoundSensor)device).getMaxdB());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set read change trigger for device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getMinSPLChangeTrigger() {
		try {
			return (float)(((SoundSensor)device).getMinSPLChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min sound pressure level (SPL) change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public float getMaxSPLChangeTrigger() {
		try {
			return (float)(((SoundSensor)device).getMaxSPLChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max sound pressure level (SPL) change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public int getSPLRange() {
		try {
			SPLRange range = ((SoundSensor)device).getSPLRange();
			switch (range) {
			case DB_102:
				return 102;
				
			case DB_82:
				return 82;
			}
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get sound pressure level (SPL) range for device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setSPLRange(int range) {
		try {
			switch (range) {
			case 102:
				((SoundSensor)device).setSPLRange(SPLRange.DB_102);
				break;
				
			case 82:
				((SoundSensor)device).setSPLRange(SPLRange.DB_82);
				break;
			
			default:
				System.err.println("SPL level can only be 82 or 102 dB. Use e.g.: <channel_name>.setSPLRange(82)");
			}
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set sound pressure level (SPL) range for device " + deviceType + " because of error: " + ex);
		}
	}
}
