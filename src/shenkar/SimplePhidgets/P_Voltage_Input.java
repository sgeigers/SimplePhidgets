package shenkar.SimplePhidgets;

import processing.core.*;
import java.lang.reflect.Method;
import com.phidget22.*;

public class P_Voltage_Input extends Device {
	// events
	Method sensorChangeEventMethod;  // sensorChange
	boolean sensorChangeFlag = false;
	boolean sensorChangeEventReportChannel = false;

	// real-time events
	Method sensorChangeEventRTMethod;  // sensorChangeRT
	boolean RTEventRegister = false;


	public P_Voltage_Input(PApplet P5Parent, Channel ChParent, String type, int serialNum, int portNum, int chNum) {
		super(P5Parent, ChParent, type, serialNum, portNum, chNum);

		// pre-opening setup
		try {
			device = new VoltageInput();
		}	catch (PhidgetException ex) {
			System.err.println("Could not open device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
			PAppletParent.exit();
		}
		
		// device opening
		switch (deviceType) {
		case "1058": // PhidgetPhSensor
			initNoHub();
			break;
			
		case "ADP1000": // pH Phidget
		case "DAQ1000": // 8x Voltage Input Phidget
			init(false);
			break;
			
		default:
			init(true);
			break;
		}

		// post-opening setup
		try {
			if (!deviceType.equals("MOT2002")) {
				// set maximum data rate as default, but not for MOT2002 - this may causes problems...
				((VoltageInput)device).setDataInterval(((VoltageInput)device).getMinDataInterval());
			}
			// set sensor type - for "sensorValue" function and "sensorChange" event.
			switch (deviceType) {
			case "1114": // Temperature Sensor
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_1114);
				break;
			case "1117": // Voltage Sensor
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_1117);
				break;
			case "1123": // Precision Voltage Sensor
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_1123);
				break;
			case "1127": // Precision Light Sensor
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_1127);
				break;
			case "1132": // 4-20mA Adapter
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_1132);
				break;
			case "1133": // Sound Sensor
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_1133);
				break;
			case "1135": // Precision Voltage Sensor
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_1135);
				break;
			case "1142": // Light Sensor 1000 lux
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_1142);
				break;
			case "1143": // Light Sensor 70000 lux
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_1143);
				break;
			case "3500": // AC Current Sensor 10Amp
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_3500);
				break;
			case "3501": // AC Current Sensor 25Amp
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_3501);
				break;
			case "3502": // AC Current Sensor 50Amp
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_3502);
				break;
			case "3503": // AC Current Sensor 100Amp
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_3503);
				break;
			case "3507": // AC Voltage Sensor 0-250V (50Hz)
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_3507);
				break;
			case "3508": // AC Voltage Sensor 0-250V (60Hz)
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_3508);
				break;
			case "3509": // DC Voltage Sensor 0-200V
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_3509);
				break;
			case "3510": // DC Voltage Sensor 0-75V
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_3510);
				break;
			case "3511": // DC Current Sensor 0-10mA
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_3511);
				break;
			case "3512": // DC Current Sensor 0-100mA
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_3512);
				break;
			case "3513": // DC Current Sensor 0-1A
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_3513);
				break;
			case "3514": // AC Active Power Sensor 0-250V*0-30A (50Hz)
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_3514);
				break;
			case "3515": // AC Active Power Sensor 0-250V*0-30A (60Hz)
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_3515);
				break;
			case "3516": // AC Active Power Sensor 0-250V*0-5A (50Hz)
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_3516);
				break;
			case "3517": // AC Active Power Sensor 0-250V*0-5A (60Hz)
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_3517);
				break;
			case "3518": // AC Active Power Sensor 0-110V*0-5A (60Hz)
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_3518);
				break;
			case "3519": // AC Active Power Sensor 0-110V*0-15A (60Hz)
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_3519);
				break;
			case "3584": // 0-50A DC Current Transducer
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_3584);
				break;
			case "3585": // 0-100A DC Current Transducer
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_3585);
				break;
			case "3586": // 0-250A DC Current Transducer
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_3586);
				break;
			case "3587": // +-50A DC Current Transducer
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_3587);
				break;
			case "3588": // +-100A DC Current Transducer
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_3588);
				break;
			case "3589": // +-250A DC Current Transducer
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_3589);
				break;
			case "MOT2002": // Motion Sensor (PIR) - set to medium sensitivity as default
				((VoltageInput)device).setSensorType(com.phidget22.VoltageSensorType.PN_MOT2002_MED);
				break;
			default:
				break;
			}
		}	catch (PhidgetException ex) {
			System.err.println("Could not set data interval and sensor type for device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
		}
		attachListeners();
	}

	// check if "sensorChange()" or "sensorChangeRT()" were defined in the sketch and create a listener for it.
	// also - if sensor type changes, the event might need to change between VoltageChange and SensorValueChange
	void attachListeners() {
		// sensorChange()
		try {
			sensorChangeEventMethod =  PAppletParent.getClass().getMethod("sensorChange");
			if (sensorChangeEventMethod != null) {
				sensorChangeEventReportChannel = false;
				if (((VoltageInput)device).getSensorType() == VoltageSensorType.VOLTAGE) {
					((VoltageInput)device).addVoltageChangeListener(new VoltageInputVoltageChangeListener() {
						public void onVoltageChange(VoltageInputVoltageChangeEvent  e) {
							//System.out.println(e.toString());
							sensorChangeFlag = true;
						}
					});
				}
				else {
					((VoltageInput)device).addSensorChangeListener(new VoltageInputSensorChangeListener() {
						public void onSensorChange(VoltageInputSensorChangeEvent  e) {
							//System.out.println(e.toString());
							sensorChangeFlag = true;
						}
					});
				}
			}
		} catch (Exception e) {
			// function "sensorChange" not defined
		}

		// sensorChange(Channel)
		try {
			sensorChangeEventMethod =  PAppletParent.getClass().getMethod("sensorChange", new Class<?>[] { Channel.class });
			if (sensorChangeEventMethod != null) {
				sensorChangeEventReportChannel = true;
				if (((VoltageInput)device).getSensorType() == VoltageSensorType.VOLTAGE) {
					((VoltageInput)device).addVoltageChangeListener(new VoltageInputVoltageChangeListener() {
						public void onVoltageChange(VoltageInputVoltageChangeEvent  e) {
							//System.out.println(e.toString());
							sensorChangeFlag = true;
						}
					});
				}
				else {
					((VoltageInput)device).addSensorChangeListener(new VoltageInputSensorChangeListener() {
						public void onSensorChange(VoltageInputSensorChangeEvent  e) {
							//System.out.println(e.toString());
							sensorChangeFlag = true;
						}
					});
				}
			}
		} catch (Exception e) {
			// function "sensorChange(Channel)" not defined
		}

		// sensorChangeRT()
		try {
			sensorChangeEventRTMethod =  PAppletParent.getClass().getMethod("sensorChangeRT");
			if (sensorChangeEventRTMethod != null) {
				if (sensorChangeEventMethod != null) {
					System.err.println("Cannot use both sensorChange() and sensorChangeRT(Channel)."); 
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
			// function "sensorChangeRT(Channel)" not defined
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
	public void pre() {
		if (RTEventRegister) {
			RTEventRegister = false;
			try {
				if (sensorChangeEventReportChannel) { // sensorChangeRT(Channel)
					if (((VoltageInput)device).getSensorType() == VoltageSensorType.VOLTAGE) {
						((VoltageInput)device).addVoltageChangeListener(new VoltageInputVoltageChangeListener() {
							public void onVoltageChange(VoltageInputVoltageChangeEvent  e) {
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
					else {
						((VoltageInput)device).addSensorChangeListener(new VoltageInputSensorChangeListener() {
							public void onSensorChange(VoltageInputSensorChangeEvent  e) {
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
				}
				else { // sensorChangeRT()
					if (((VoltageInput)device).getSensorType() == VoltageSensorType.VOLTAGE) {
						((VoltageInput)device).addVoltageChangeListener(new VoltageInputVoltageChangeListener() {
							public void onVoltageChange(VoltageInputVoltageChangeEvent  e) {
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
					else {
						((VoltageInput)device).addSensorChangeListener(new VoltageInputSensorChangeListener() {
							public void onSensorChange(VoltageInputSensorChangeEvent  e) {
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
				}
			} catch (Exception ex) {
		    	System.err.println("Disabling sensorChangeRT() for " + deviceType + " because of an error:");
		    	ex.printStackTrace();
		    	sensorChangeEventRTMethod = null;
		    }
		}
	}

	@Override
	public int read() {
		try {
			double val =((VoltageInput)device).getVoltage();
			val -= ((VoltageInput)device).getMinVoltage();
			val /= ((VoltageInput)device).getMaxVoltage() - ((VoltageInput)device).getMinVoltage();
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
			return ((VoltageInput)device).getDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setDataInterval(int dataInterval) {
		try {
			((VoltageInput)device).setDataInterval(dataInterval);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set data interval value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public int getMinDataInterval() {
		try {
			return ((VoltageInput)device).getMinDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public int getMaxDataInterval() {
		try {
			return ((VoltageInput)device).getMaxDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public int getPowerSupply() {  // in V; 12, 24 or 0 for OFF
		if (deviceType.equals("DAQ1400")) {
			try {
				PowerSupply ps = (((VoltageInput)device).getPowerSupply());
				switch (ps) {
				case OFF:
					return 0;
				case VOLTS_12:
					return 12;
				case VOLTS_24:
					return 24;
				}
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get power supply from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getPowerSupply() is not valid for device of type " + deviceType);				
		}
		return 0;
	}

	@Override
	public void setPowerSupply(int ps) {
		if (deviceType.equals("DAQ1400")) {
			try {
				PowerSupply p = PowerSupply.OFF;
				switch (ps) {
				case 12:
					p = PowerSupply.VOLTS_12;
					break;
				case 24:
					p = PowerSupply.VOLTS_24;
					break;
				case 0:
					break;
				default:
					System.err.println("Invalid power supply: " + ps + ". Use power supply type in Volts, and only 12, 24 or 0 to turn off");
					break;						
				}
				((VoltageInput)device).setPowerSupply(p);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set power supply to device " + deviceType);
			}
		}
		else {
			System.err.println("setPowerSupply(int) is not valid for device of type " + deviceType);							
		}
	}

	@Override
	public String getSensorType() {
		if (deviceType.equals("1058")) {
			System.err.println("getSensorType() is not valid for device of type " + deviceType);				
		}
		else {
			try {
				VoltageSensorType sensorType = ((VoltageInput)device).getSensorType();
				switch (sensorType) {
				case VOLTAGE: return "VOLTAGE"; // Generic voltage sensor
				case PN_1114: return "1114"; // Temperature Sensor
				case PN_1117: return "1117"; // Voltage Sensor
				case PN_1123: return "1123"; // Precision Voltage Sensor
				case PN_1127: return "1127"; // Precision Light Sensor
				case PN_1130_PH: return "1130"; // pH Adapter
				case PN_1130_ORP: return "1130"; // ORP Adapter
				case PN_1132: return "1132"; // 4-20mA Adapter
				case PN_1133: return "1133"; // Sound Sensor
				case PN_1135: return "1135"; // Precision Voltage Sensor
				case PN_1142: return "1142"; // Light Sensor 1000 lux
				case PN_1143: return "1143"; // Light Sensor 70000 lux
				case PN_3500: return "3500"; // AC Current Sensor 10Amp
				case PN_3501: return "3501"; // AC Current Sensor 25Amp
				case PN_3502: return "3502"; // AC Current Sensor 50Amp
				case PN_3503: return "3503"; // AC Current Sensor 100Amp
				case PN_3507: return "3507"; // AC Voltage Sensor 0-250V (50Hz)
				case PN_3508: return "3508"; // AC Voltage Sensor 0-250V (60Hz)
				case PN_3509: return "3509"; // DC Voltage Sensor 0-200V
				case PN_3510: return "3510"; // DC Voltage Sensor 0-75V
				case PN_3511: return "3511"; // DC Current Sensor 0-10mA
				case PN_3512: return "3512"; // DC Current Sensor 0-100mA
				case PN_3513: return "3513"; // DC Current Sensor 0-1A
				case PN_3514: return "3514"; // AC Active Power Sensor 0-250V*0-30A (50Hz)
				case PN_3515: return "3515"; // AC Active Power Sensor 0-250V*0-30A (60Hz)
				case PN_3516: return "3516"; // AC Active Power Sensor 0-250V*0-5A (50Hz)
				case PN_3517: return "3517"; // AC Active Power Sensor 0-250V*0-5A (60Hz)
				case PN_3518: return "3518"; // AC Active Power Sensor 0-110V*0-5A (60Hz)
				case PN_3519: return "3519"; // AC Active Power Sensor 0-110V*0-15A (60Hz)
				case PN_3584: return "3584"; // 0-50A DC Current Transducer
				case PN_3585: return "3585"; // 0-100A DC Current Transducer
				case PN_3586: return "3586"; // 0-250A DC Current Transducer
				case PN_3587: return "3587"; // +-50A DC Current Transducer
				case PN_3588: return "3588"; // +-100A DC Current Transducer
				case PN_3589: return "3589"; // +-250A DC Current Transducer
				case PN_MOT2002_LOW: return "MOT2002"; // Motion Sensor Low Sensitivity
				case PN_MOT2002_MED: return "MOT2002"; // Motion Sensor Medium Sensitivity
				case PN_MOT2002_HIGH: return "MOT2002"; // Motion Sensor High Sensitivity
				case PN_VCP4114: return "VCP4114"; // Clip-on Current Transducer 25A
				}
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get sensor type from device " + deviceType + " because of error: " + ex);
			}
		}
		return "";
	}

	@Override
	public void setSensorType(String sensorType) {
		if (deviceType.equals("1058")) {
			System.err.println("setSensorType(String) is not valid for device of type " + deviceType);				
		}
		else {
			VoltageSensorType nSensorType = VoltageSensorType.VOLTAGE;

			sensorType = sensorType.toUpperCase();
			int p = sensorType.indexOf("_");
			String hSensorType = sensorType;
			if (p > 0) hSensorType = sensorType.substring(0, p);

			switch (hSensorType) {
			case "VOLTAGE": // Generic voltage sensor, or sensor type not set yet
				break;
			case "1114":  // Temperature Sensor
				nSensorType = VoltageSensorType.PN_1114;
				break;
			case "1117":  // Voltage Sensor
				nSensorType = VoltageSensorType.PN_1117;
				break;
			case "1123":  // Precision Voltage Sensor
				nSensorType = VoltageSensorType.PN_1123;
				break;
			case "1127":  // Precision Light Sensor
				nSensorType = VoltageSensorType.PN_1127;
				break;
			case "1130":
				if (p > 0) {
					String tSensorType = sensorType.substring(p);
					switch (tSensorType) {
					case "_PH":  // pH Adapter
						nSensorType = VoltageSensorType.PN_1130_PH;
						break;
					case "_ORP":  // ORP Adapter
						nSensorType = VoltageSensorType.PN_1130_ORP;
						break;
					default:	
						System.out.println("For using sensor value with pH/ORP adapter, specify type of electrode used. e.g. \"pH\"");
						break;
					}
				}
				else {
					System.out.println("For using sensor value with pH/ORP adapter, specify type of electrode used. e.g. \"pH\"");
				}
				return;
			case "PH":  // pH Adapter
			case "1130PH":
				nSensorType = VoltageSensorType.PN_1130_PH;
				break;
			case "ORP":  // ORP Adapter
			case "1130ORP":
				nSensorType = VoltageSensorType.PN_1130_ORP;
				break;
			case "1132": // 4-20mA Adapter
				nSensorType = VoltageSensorType.PN_1132;
				break;
			case "1133": // Sound Sensor
				nSensorType = VoltageSensorType.PN_1133;
				break;
			case "1135": // Precision Voltage Sensor
				nSensorType = VoltageSensorType.PN_1135;
				break;
			case "1142": // Light Sensor 1000 lux
				nSensorType = VoltageSensorType.PN_1142;
				break;
			case "1143": // Light Sensor 70000 lux
				nSensorType = VoltageSensorType.PN_1143;
				break;
			case "3500": // AC Current Sensor 10Amp
				nSensorType = VoltageSensorType.PN_3500;
				break;
			case "3501": // AC Current Sensor 25Amp
				nSensorType = VoltageSensorType.PN_3501;
				break;
			case "3502": // AC Current Sensor 50Amp
				nSensorType = VoltageSensorType.PN_3502;
				break;
			case "3503": // AC Current Sensor 100Amp
				nSensorType = VoltageSensorType.PN_3503;
				break;
			case "3507": // AC Voltage Sensor 0-250V (50Hz)
				nSensorType = VoltageSensorType.PN_3507;
				break;
			case "3508": // AC Voltage Sensor 0-250V (60Hz)
				nSensorType = VoltageSensorType.PN_3508;
				break;
			case "3509": // DC Voltage Sensor 0-200V
				nSensorType = VoltageSensorType.PN_3509;
				break;
			case "3510": // DC Voltage Sensor 0-75V
				nSensorType = VoltageSensorType.PN_3510;
				break;
			case "3511": // DC Current Sensor 0-10mA
				nSensorType = VoltageSensorType.PN_3511;
				break;
			case "3512": // DC Current Sensor 0-100mA
				nSensorType = VoltageSensorType.PN_3512;
				break;
			case "3513": // DC Current Sensor 0-1A
				nSensorType = VoltageSensorType.PN_3513;
				break;
			case "3514": // AC Active Power Sensor 0-250V*0-30A (50Hz)
				nSensorType = VoltageSensorType.PN_3514;
				break;
			case "3515": // AC Active Power Sensor 0-250V*0-30A (60Hz)
				nSensorType = VoltageSensorType.PN_3515;
				break;
			case "3516": // AC Active Power Sensor 0-250V*0-5A (50Hz)
				nSensorType = VoltageSensorType.PN_3516;
				break;
			case "3517": // AC Active Power Sensor 0-250V*0-5A (60Hz)
				nSensorType = VoltageSensorType.PN_3517;
				break;
			case "3518": // AC Active Power Sensor 0-110V*0-5A (60Hz)
				nSensorType = VoltageSensorType.PN_3518;
				break;
			case "3519": // AC Active Power Sensor 0-110V*0-15A (60Hz)
				nSensorType = VoltageSensorType.PN_3519;
				break;
			case "3584": // 0-50A DC Current Transducer
				nSensorType = VoltageSensorType.PN_3584;
				break;
			case "3585": // 0-100A DC Current Transducer
				nSensorType = VoltageSensorType.PN_3585;
				break;
			case "3586": // 0-250A DC Current Transducer
				nSensorType = VoltageSensorType.PN_3586;
				break;
			case "3587": // +-50A DC Current Transducer
				nSensorType = VoltageSensorType.PN_3587;
				break;
			case "3588": // +-100A DC Current Transducer
				nSensorType = VoltageSensorType.PN_3588;
				break;
			case "3589": // +-250A DC Current Transducer
				nSensorType = VoltageSensorType.PN_3589;
				break;
			case "MOT2002":
				if (p > 0) {
					String tSensorType = sensorType.substring(p);
					switch (tSensorType) {
					case "_LOW": // Motion Sensor Low Sensitivity
						nSensorType = VoltageSensorType.PN_MOT2002_LOW;
						break;
					case "_MED":
					case "_MEDIUM": // Motion Sensor Medium Sensitivity
						nSensorType = VoltageSensorType.PN_MOT2002_MED;
						break;
					case "_HIGH": // Motion Sensor High Sensitivity
						nSensorType = VoltageSensorType.PN_MOT2002_HIGH;
						break;	
					default:
						System.out.println("For using sensor value with MOT2002 motion sensor, specify wanted sensitivity. e.g. \"LOW\"");
						break;						
					}
				}
				else {
					System.out.println("For using sensor value with MOT2002 motion sensor, specify wanted sensitivity. e.g. \"LOW\"");
				}
				break;		
			case "LOW": // Motion Sensor Low Sensitivity
				nSensorType = VoltageSensorType.PN_MOT2002_LOW;
				break;
			case "MED":
			case "MEDIUM": // Motion Sensor Medium Sensitivity
				nSensorType = VoltageSensorType.PN_MOT2002_MED;
				break;
			case "HIGH": // Motion Sensor High Sensitivity
				nSensorType = VoltageSensorType.PN_MOT2002_HIGH;
				break;
			case "VCP4114": // Clip-on Current Transducer 25A
				nSensorType = VoltageSensorType.PN_VCP4114;
				break;

			default:
				System.err.println("Cannot set sensor type to " + sensorType +". See documentation for correct usage");		
				return;
			}
			try {
				((VoltageInput)device).setSensorType(nSensorType);
				attachListeners();
				// if not in setup() and real-time event is registered - run the pre() function to re-create it if needed
				if (RTEventRegister && (PAppletParent.frameCount > 0)) pre();
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set sensor type for device " + deviceType + " because of error: " + ex);
			}
		}
	}

	@Override
	public String getSensorUnit() {
		if (deviceType.equals("1058")) {
			System.err.println("getSensorUnit() is not valid for device of type " + deviceType);				
		}
		else {
			try {
				return (((VoltageInput)device).getSensorUnit().name);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get sensor unit for device " + deviceType + " because of error: " + ex);
			}
		}
		return "";
	}

	@Override
	public float getSensorValue() {
		if (deviceType.equals("1058")) {
			System.err.println("getSensorValue() is not valid for device of type " + deviceType);				
		}
		else {
			try {
				if (((VoltageInput)device).getSensorType() == VoltageSensorType.VOLTAGE) {
					System.err.println("Cannot get sensor value for device " + deviceType + " because sensor type not set. use \"setSensorType\" inside setup().");
					return 0.0f;
				}
				return (float)(((VoltageInput)device).getSensorValue());
			}
			catch (PhidgetException ex) {
				if 	(ex.getErrorCode() == com.phidget22.ErrorCode.UNKNOWN_VALUE) {
					System.err.println("Sensor value out of range");
				}
				else {
					System.err.println("Cannot get sensor value for device " + deviceType + " because of error: " + ex);
					PAppletParent.exit();
				}
			}
		}
		return 0.0f;
	}

	@Override
	public boolean getSensorValueValidity() {
		if (deviceType.equals("1058")) {
			System.err.println("getSensorValueValidity() is not valid for device of type " + deviceType);				
		}
		else {
			try {
				if (((VoltageInput)device).getSensorType() == VoltageSensorType.VOLTAGE) {
					System.err.println("Cannot check sensor value validity for device " + deviceType + " because sensor type not set. use \"setSensorType\" inside setup().");
					return false;
				}
				@SuppressWarnings("unused")
				double reading = ((VoltageInput)device).getSensorValue();
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
		}
		return false;
	}

	@Override
	public float getSensorValueChangeTrigger() {
		if (deviceType.equals("1058")) {
			System.err.println("getSensorValueChangeTrigger() is not valid for device of type " + deviceType);				
		}
		else {
			try {
				return (float)(((VoltageInput)device).getSensorValueChangeTrigger());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get sensor value change trigger for device " + deviceType + " because of error: " + ex);
			}
		}
		return 0.0f;
	}

	@Override
	public void setSensorValueChangeTrigger(float sensorValueChangeTrigger) {
		if (deviceType.equals("1058")) {
			System.err.println("setSensorValueChangeTrigger() is not valid for device of type " + deviceType);				
		}
		else {
			try {
				((VoltageInput)device).setSensorValueChangeTrigger((double)sensorValueChangeTrigger);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set sensor value change trigger for device " + deviceType + " because of error: " + ex);
			}
		}
	}

	@Override
	public float getVoltage() {
		try {
			return (float)(((VoltageInput)device).getVoltage());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get voltage for device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0.0f;
	}

	@Override
	public float getMinVoltage() {
		try {
			return (float)(((VoltageInput)device).getMinVoltage());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min voltage for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public float getMaxVoltage() {
		try {
			return (float)(((VoltageInput)device).getMaxVoltage());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max voltage for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public float getVoltageChangeTrigger() {
		try {
			return (float)(((VoltageInput)device).getVoltageChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get voltage change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public void setVoltageChangeTrigger(float voltageChangeTrigger) {
		try {
			((VoltageInput)device).setVoltageChangeTrigger((double)voltageChangeTrigger);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set voltage ratio change trigger for device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public void setReadChangeTrigger(int readChangeTrigger) {
		try {
			double val = (double)readChangeTrigger / 1000.0;
			val *= ((VoltageInput)device).getMaxVoltage() - ((VoltageInput)device).getMinVoltage();
			val += ((VoltageInput)device).getMinVoltage();
			((VoltageInput)device).setVoltageChangeTrigger(val);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set read change trigger for device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getMinVoltageChangeTrigger() {
		try {
			return (float)(((VoltageInput)device).getMinVoltageChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min voltage change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public float getMaxVoltageChangeTrigger() {
		try {
			return (float)(((VoltageInput)device).getMaxVoltageChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max voltage change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public int getVoltageRange() {  // in mV;  312 => 312.5;  -1 => AUTO
		if (deviceType.equals("1058")) {
			System.err.println("getVoltageRange() is not valid for device of type " + deviceType);				
		}
		else {
			try {
				VoltageRange vr = (((VoltageInput)device).getVoltageRange());
				switch (vr) {
				case MILLIVOLTS_10:
					return 10;
				case MILLIVOLTS_40:
					return 40;
				case MILLIVOLTS_200:
					return 200;
				case MILLIVOLTS_312_5:
					return 312;
				case MILLIVOLTS_400:
					return 400;
				case MILLIVOLTS_1000:
					return 1000;
				case VOLTS_2:
					return 2000;
				case VOLTS_5:
					return 5000;
				case VOLTS_15:
					return 15000;
				case VOLTS_40:
					return 40000;
				case AUTO:
					return -1;
				}
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get voltage range from device " + deviceType + " because of error: " + ex);
			}
		}
		return 0;
	}

	@Override
	public void setVoltageRange(int vr) {
		if (deviceType.equals("1058")) {
			System.err.println("setVoltageRange(int) is not valid for device of type " + deviceType);				
		}
		else {
			try {
				VoltageRange v = VoltageRange.AUTO;
				switch (vr) {
				case 10:
					v = VoltageRange.MILLIVOLTS_10;
					break;
				case 40:
					v = VoltageRange.MILLIVOLTS_40;
					break;
				case 200:
					v = VoltageRange.MILLIVOLTS_200;
					break;
				case 312:
					v = VoltageRange.MILLIVOLTS_312_5;
					break;
				case 400:
					v = VoltageRange.MILLIVOLTS_400;
					break;
				case 1000:
					v = VoltageRange.MILLIVOLTS_1000;
					break;
				case 2000:
					v = VoltageRange.VOLTS_2;
					break;
				case 5000:
					v = VoltageRange.VOLTS_5;
					break;
				case 15000:
					v = VoltageRange.VOLTS_15;
					break;
				case 40000:
					v = VoltageRange.VOLTS_40;
					break;
				case -1:
					break;
				default:
					System.err.println("Invalid range: " + vr + ". Use range in mV, and only 10, 40, 200, 312 (for 312.5mV), 400, 1000, 2000, 5000, 15000, 40000 or -1 for AUTO");
					break;						
				}
				((VoltageInput)device).setVoltageRange(v);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set voltage range to device " + deviceType);
			}
		}
	}
}
