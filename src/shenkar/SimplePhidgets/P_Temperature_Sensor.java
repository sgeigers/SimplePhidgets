package shenkar.SimplePhidgets;

import processing.core.*;
import java.lang.reflect.Method;
import com.phidget22.*;

public class P_Temperature_Sensor extends Device {
	// events
	Method sensorChangeEventMethod;  // sensorChange
	boolean sensorChangeFlag = false;
	boolean sensorChangeEventReportChannel = false;
	
	// real-time events
	Method sensorChangeEventRTMethod;  // sensorChangeRT
	boolean RTEventRegister = false;

	public P_Temperature_Sensor(PApplet P5Parent, Channel ChParent, String type, int serialNum, int portNum, int chNum) {
		super(P5Parent, ChParent, type, serialNum, portNum, chNum);

		// pre-opening setup
		try {
			device = new TemperatureSensor();
		}	catch (PhidgetException ex) {
			System.err.println("Could not open device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
			PAppletParent.exit();
		}
				
		// device opening
		switch (deviceType) {
		case "1045": // PhidgetTemperatureSensor IR			[only dataInterval, Temperature, TemperatureChangeTrigger]
		case "1048": // PhidgetTemperatureSensor 4-Input	[only dataInterval, Temperature, TemperatureChangeTrigger, thermocoupleType]
		case "1051": // PhidgetTemperatureSensor 1-Input	[only dataInterval, Temperature, TemperatureChangeTrigger, thermocoupleType]
			initNoHub();
			break;

		case "DCC1000": // DC Motor Phidget
		case "DCC1100": // Brushless DC Motor Phidget
		case "HUM1000": // Humidity Phidget					[only dataInterval, Temperature, TemperatureChangeTrigger]
		case "HUM1001": // Humidity Phidget					[only dataInterval, Temperature, TemperatureChangeTrigger]
		case "MOT0109": // PhidgetSpatial Precision 3/3/3	[only dataInterval, Temperature, TemperatureChangeTrigger]
		case "SAF1000": // Programmable Power Guard Phidget	[only dataInterval, Temperature, TemperatureChangeTrigger]
		case "TMP1000": // Temperature Phidget				[only dataInterval, Temperature, TemperatureChangeTrigger]
		case "TMP1101": // 4x Thermocouple Phidget			[only dataInterval, Temperature, TemperatureChangeTrigger, thermocoupleType]
		case "TMP1200": // RTD Phidget						[only dataInterval, RTDType, RTDWireSetup, Temperature, TemperatureChangeTrigger]
			init(false);
			break;
			
		default:
			System.out.println("Device type " + deviceType + " not supported yet");
			PAppletParent.exit();
			break;
			
		}

		// post-opening setup
		try {
			// set maximum data rate as default
			((TemperatureSensor)device).setDataInterval(((TemperatureSensor)device).getMinDataInterval());
		}
		catch (PhidgetException ex) {
			System.err.println("Could not set data interval for device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
		}
		attachListeners();
	}

	// check if "sensorChange()" was defined in the sketch and create a listener for it.
	void attachListeners() {
		// sensorChange()
		try {
			sensorChangeEventMethod =  PAppletParent.getClass().getMethod("sensorChange");
			if (sensorChangeEventMethod != null) {
				sensorChangeEventReportChannel = false;
				((TemperatureSensor)device).addTemperatureChangeListener(new TemperatureSensorTemperatureChangeListener() {
					public void onTemperatureChange(TemperatureSensorTemperatureChangeEvent  e) {
						//System.out.println(e.toString());
						sensorChangeFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "sensorChange()" not defined
		}

		// sensorChange(Channel)
		try {
			sensorChangeEventMethod =  PAppletParent.getClass().getMethod("sensorChange", new Class<?>[] { Channel.class });
			if (sensorChangeEventMethod != null) {
				sensorChangeEventReportChannel = true;
				((TemperatureSensor)device).addTemperatureChangeListener(new TemperatureSensorTemperatureChangeListener() {
					public void onTemperatureChange(TemperatureSensorTemperatureChangeEvent  e) {
						//System.out.println(e.toString());
						sensorChangeFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "sensorChange(Channel)" not defined
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
			// function "sensorChangeRT()" not defined
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
			// function "sensorChangeRT(Channel)" not defined
		}
	}

	@Override
	public void pre() {
		if (RTEventRegister) {
			RTEventRegister = false;
			try {
				if (sensorChangeEventReportChannel) { // sensorChangeRT(Channel)
					((TemperatureSensor)device).addTemperatureChangeListener(new TemperatureSensorTemperatureChangeListener() {
						public void onTemperatureChange(TemperatureSensorTemperatureChangeEvent  e) {
							//System.out.println(e.toString());
							try {
								if (sensorChangeEventRTMethod != null) {
									sensorChangeEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
								}
							} catch (Exception ex) {
								System.err.println("Disabling sensorChange() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								sensorChangeEventRTMethod = null;
							}
						}
					});
				}
				else { // sensorChangeRT()
					((TemperatureSensor)device).addTemperatureChangeListener(new TemperatureSensorTemperatureChangeListener() {
						public void onTemperatureChange(TemperatureSensorTemperatureChangeEvent  e) {
							//System.out.println(e.toString());
							try {
								if (sensorChangeEventRTMethod != null) {
									sensorChangeEventRTMethod.invoke(PAppletParent);
								}
							} catch (Exception ex) {
								System.err.println("Disabling sensorChange() for " + deviceType + " because of an error:");
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
			double val =((TemperatureSensor)device).getTemperature();
			return (int)val;
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get temperature (int read) from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0; 
	}

	@Override
	public float getTemperature() {
		try {
			double val =((TemperatureSensor)device).getTemperature();
			return (float)val;
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get temperature from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0; 
	}

	@Override
	public float getSensorValue() {
		try {
			double val =((TemperatureSensor)device).getTemperature();
			return (float)val;
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get temperature from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0; 
	}

	@Override
	public float getMinTemperature() {
		try {
			double val =((TemperatureSensor)device).getMinTemperature();
			return (float)val;
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get minimum temperature from device " + deviceType + " because of error: " + ex);
		}
		return 0; 
	}

	@Override
	public float getMaxTemperature() {
		try {
			double val =((TemperatureSensor)device).getMaxTemperature();
			return (float)val;
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get maximum temperature from device " + deviceType + " because of error: " + ex);
		}
		return 0; 
	}

	@Override
	public boolean getSensorValueValidity() {
		try {
			@SuppressWarnings("unused")
			double val =((TemperatureSensor)device).getTemperature();
			return true;
		}
		catch (PhidgetException ex) {
			if 	((ex.getErrorCode() == com.phidget22.ErrorCode.UNKNOWN_VALUE) || (ex.getErrorCode() == com.phidget22.ErrorCode.UNEXPECTED)) {
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
	public int getDataInterval() {
		try {
			return ((TemperatureSensor)device).getDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setDataInterval(int dataInterval) {
		try {
			((TemperatureSensor)device).setDataInterval(dataInterval);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set data interval value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public int getMinDataInterval() {
		try {
			return ((TemperatureSensor)device).getMinDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public int getMaxDataInterval() {
		try {
			return ((TemperatureSensor)device).getMaxDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public String getRTDType() {
		if (deviceType.equals("TMP1200")) {
			try {
				RTDType sensorType = ((TemperatureSensor)device).getRTDType();
				switch (sensorType) {
				case PT100_3850: return "PT100_3850";
				case PT1000_3850: return "PT1000_3850";
				case PT100_3920: return "PT100_3920";
				case PT1000_3920: return "PT1000_3920";
				}
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get RTD type value from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getRTDType() is not valid for device of type " + deviceType);
			PAppletParent.exit();
		}
		return "";
	}

	@Override
	public void setRTDType(String sensorType) {
		if (deviceType.equals("TMP1200")) {
			RTDType nSensorType = RTDType.PT100_3850;
			sensorType = sensorType.toUpperCase();
			switch (sensorType) {
			case "PT100_3850":
				break;
	
			case "PT1000_3850":
				nSensorType = RTDType.PT1000_3850;
				break;
	
			case "PT100_3920":
				nSensorType = RTDType.PT100_3920;
				break;
	
			case "PT1000_3920":
				nSensorType = RTDType.PT1000_3920;
				break;
	
			default:
				System.err.println("Cannot set RTD type to " + sensorType +". Use only: \"PT100_3850\", \"PT1000_3850\", \"PT100_3920\" or \"PT1000_3920\"");		
				return;
			}
			try {
				((TemperatureSensor)device).setRTDType(nSensorType);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set RTD type for device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("setRTDType(String) is not valid for device of type " + deviceType);
			PAppletParent.exit();
		}
	}

	@Override
	public int getRTDWireSetup() {
		if (deviceType.equals("TMP1200")) {
			try {
				RTDWireSetup wireSetup = ((TemperatureSensor)device).getRTDWireSetup();
				switch (wireSetup) {
				case WIRES_2: return 2;
				case WIRES_3: return 3;
				case WIRES_4: return 4;
				}
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get RTD type value from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getRTDWireSetup() is not valid for device of type " + deviceType);
			PAppletParent.exit();
		}
		return 0;
	}

	@Override
	public void setRTDWireSetup(int setup) {
		if (deviceType.equals("TMP1200")) {
			RTDWireSetup nSetup = RTDWireSetup.WIRES_2;
			switch (setup) {
			case 2:
				break;
	
			case 3:
				nSetup = RTDWireSetup.WIRES_3;
				break;
	
			case 4:
				nSetup = RTDWireSetup.WIRES_4;
				break;
	
			default:
				System.err.println("Cannot set RTD wire setup to " + setup +". Use only: 2, 3 or 4 (for 2-wire, 3-wire or 4-wire)");		
				return;
			}
			try {
				((TemperatureSensor)device).setRTDWireSetup(nSetup);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set RTD wire setup for device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("setRTDWireSetup(int) is not valid for device of type " + deviceType);
			PAppletParent.exit();
		}
	}

	@Override
	public float getTemperatureChangeTrigger() {
		try {
			return (float)(((TemperatureSensor)device).getTemperatureChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get temperature change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public float getSensorValueChangeTrigger() {
		try {
			return (float)(((TemperatureSensor)device).getTemperatureChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get sensor value change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public void setTemperatureChangeTrigger(float changeTrigger) {
		try {
			((TemperatureSensor)device).setTemperatureChangeTrigger((double)changeTrigger);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set temperature change trigger for device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public void setSensorValueChangeTrigger(float changeTrigger) {
		try {
			((TemperatureSensor)device).setTemperatureChangeTrigger((double)changeTrigger);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set temperature change trigger for device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getMinTemperatureChangeTrigger() {
		try {
			return (float)(((TemperatureSensor)device).getMinTemperatureChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get minimum temperature change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public float getMaxTemperatureChangeTrigger() {
		try {
			return (float)(((TemperatureSensor)device).getMaxTemperatureChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get maximum temperature change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public String getSensorUnit() {
		return "DEGREE_CELSIUS";
	}

	@Override
	public String getThermocoupleType() {
		if (deviceType.equals("1048") || deviceType.equals("1051") || deviceType.equals("TMP1101")) {
			try {
				ThermocoupleType tcType = ((TemperatureSensor)device).getThermocoupleType();
				switch (tcType) {
				case J: return "J";
				case K: return "K";
				case E: return "E";
				case T: return "T";
				}
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get thermocouple type from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getThermocoupleType() is not valid for device of type " + deviceType);
			PAppletParent.exit();
		}
		return "";
	}

	@Override
	public void setThermocoupleType(String tcType) {
		if (deviceType.equals("1048") || deviceType.equals("1051") || deviceType.equals("TMP1101")) {
			ThermocoupleType ntcType = ThermocoupleType.J;
	
			tcType = tcType.toUpperCase();
	
			switch (tcType) {
			case "J":
				break;
	
			case "K":
				ntcType = ThermocoupleType.K;
				break;
	
			case "E":
				ntcType = ThermocoupleType.E;
				break;
	
			case "T":
				ntcType = ThermocoupleType.T;
				break;
	
			default:
				System.err.println("Cannot set thermocouple type to " + ntcType +". Use only: \"J\", \"K\", \"E\" or \"T\"");		
				return;
			}
			try {
				((TemperatureSensor)device).setThermocoupleType(ntcType);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set RTD type for device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("setThermocoupleType(String) is not valid for device of type " + deviceType);
			PAppletParent.exit();
		}
	}
}
