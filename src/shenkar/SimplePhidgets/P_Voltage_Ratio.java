package shenkar.SimplePhidgets;

import processing.core.*;
import java.lang.reflect.Method;
import com.phidget22.*;

public class P_Voltage_Ratio extends Device {
	// events
	Method sensorChangeEventMethod;  // sensorChange
	boolean sensorChangeFlag = false;
	boolean sensorChangeEventReportChannel = false;
	
	// real-time events
	Method sensorChangeEventRTMethod;  // sensorChangeRT
	boolean RTEventRegister = false;

	public P_Voltage_Ratio(PApplet P5Parent, Channel ChParent, String type, int serialNum, int portNum, int chNum) {
		super(P5Parent, ChParent, type, serialNum, portNum, chNum);

		// pre-opening setup
		try {
			device = new VoltageRatioInput();
		}	catch (PhidgetException ex) {
			System.err.println("Could not open device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
			PAppletParent.exit();
		}
				
		// device opening
		switch (deviceType) {
		case "DAQ1500": // weatstone bridge
		case "DCC1000": // DC Motor Phidget
		case "HIN1100": // thumbstick
			init(false);
			break;

		case "1046": // weatstone bridge
			initNoHub();
			break;

		default:
			init(true);
			break;
		}

		// post-opening setup
		try {
			// set maximum data rate as default
			((VoltageRatioInput)device).setDataInterval(((VoltageRatioInput)device).getMinDataInterval());

			// set sensor type - for "sensorValue" function and "sensorChange" event.
			switch (deviceType) {
			case "1102": // IR Reflective Sensor 5mm
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1102);
				break;
			case "1103": // IR Reflective Sensor 10cm
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1103);
				break;
			case "1104": // Vibration Sensor
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1104);
				break;
			case "1105": // Light Sensor
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1105);
				break;
			case "1106": // Force Sensor
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1106);
				break;
			case "1107": // Humidity Sensor
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1107);
				break;
			case "1108": // Magnetic Sensor
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1108);
				break;
			case "1109": // Rotation Sensor
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1109);
				break;
			case "1110": // Touch Sensor
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1110);
				break;
			case "1111": // Motion Sensor (PIR)
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1111);
				break;
			case "1112": // Slider 60
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1112);
				break;
			case "1113": // Mini Joy Stick Sensor
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1113);
				break;
			case "1115": // Pressure Sensor
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1115);
				break;
			case "1116": // Multi-turn Rotation Sensor
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1116);
				break;
			case "1120": // FlexiForce Adapter
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1120);
				break;
			case "1121": // Voltage Divider
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1121);
				break;
			case "1124": // Precision Temperature Sensor
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1124);
				break;
			case "1126": // Differential Air Pressure Sensor +- 25kPa
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1126);
				break;
			case "1128": // MaxBotix EZ-1 Sonar Sensor
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1128);
				break;
			case "1129": // Touch Sensor
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1129);
				break;
			case "1131": // Thin Force Sensor
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1131);
				break;
			case "1134": // Switchable Voltage Divider
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1134);
				break;
			case "1136": // Differential Air Pressure Sensor +-2 kPa
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1136);
				break;
			case "1137": // Differential Air Pressure Sensor +-7 kPa
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1137);
				break;
			case "1138": // Differential Air Pressure Sensor 50 kPa
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1138);
				break;
			case "1139": // Differential Air Pressure Sensor 100 kPa
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1139);
				break;
			case "1140": // Absolute Air Pressure Sensor 20-400 kPa
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1140);
				break;
			case "1141": // Absolute Air Pressure Sensor 15-115 kPa
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1141);
				break;
			case "1146": // IR Reflective Sensor 1-4mm
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_1146);
				break;
			case "3120": // Compression Load Cell (0-4.5 kg)
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_3120);
				break;
			case "3121": // Compression Load Cell (0-11.3 kg)
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_3121);
				break;
			case "3122": // Compression Load Cell (0-22.7 kg)
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_3122);
				break;
			case "3123": // Compression Load Cell (0-45.3 kg)
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_3123);
				break;
			case "3130": // Relative Humidity Sensor
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_3130);
				break;
			case "3520": // Sharp Distance Sensor (4-30cm)
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_3520);
				break;
			case "3521": // Sharp Distance Sensor (10-80cm)
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_3521);
				break;
			case "3522": // Sharp Distance Sensor (20-150cm)
				((VoltageRatioInput)device).setSensorType(VoltageRatioSensorType.PN_3522);
				break;
			default:
				break;
			}
		}	catch (PhidgetException ex) {
			System.err.println("Could not set data interval for device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
		}
		attachListeners();
	}

	// check if "sensorChange()" or "sensorChangeRT()" were defined in the sketch and create a listener for it.
	// also - if sensor type changes, the event might need to change between VoltageRatioChange and SensorValueChange
	void attachListeners() {
		// sensorChange()
		try {
			sensorChangeEventMethod =  PAppletParent.getClass().getMethod("sensorChange");
			if (sensorChangeEventMethod != null) {
				sensorChangeEventReportChannel = false;
				if (((VoltageRatioInput)device).getSensorType() == VoltageRatioSensorType.VOLTAGE_RATIO) {
					((VoltageRatioInput)device).addVoltageRatioChangeListener(new VoltageRatioInputVoltageRatioChangeListener() {
						public void onVoltageRatioChange(VoltageRatioInputVoltageRatioChangeEvent  e) {
							//System.out.println(e.toString());
							sensorChangeFlag = true;
						}
					});
				}
				else {
					((VoltageRatioInput)device).addSensorChangeListener(new VoltageRatioInputSensorChangeListener() {
						public void onSensorChange(VoltageRatioInputSensorChangeEvent e) {
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
				if (((VoltageRatioInput)device).getSensorType() == VoltageRatioSensorType.VOLTAGE_RATIO) {
					((VoltageRatioInput)device).addVoltageRatioChangeListener(new VoltageRatioInputVoltageRatioChangeListener() {
						public void onVoltageRatioChange(VoltageRatioInputVoltageRatioChangeEvent  e) {
							//System.out.println(e.toString());
							sensorChangeFlag = true;
						}
					});
				}
				else {
					((VoltageRatioInput)device).addSensorChangeListener(new VoltageRatioInputSensorChangeListener() {
						public void onSensorChange(VoltageRatioInputSensorChangeEvent e) {
							//System.out.println(e.toString());
							sensorChangeFlag = true;
						}
					});
				}
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
					if (((VoltageRatioInput)device).getSensorType() == VoltageRatioSensorType.VOLTAGE_RATIO) {
						((VoltageRatioInput)device).addVoltageRatioChangeListener(new VoltageRatioInputVoltageRatioChangeListener() {
							public void onVoltageRatioChange(VoltageRatioInputVoltageRatioChangeEvent  e) {								
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
						((VoltageRatioInput)device).addSensorChangeListener(new VoltageRatioInputSensorChangeListener() {
							public void onSensorChange(VoltageRatioInputSensorChangeEvent e) {
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
					if (((VoltageRatioInput)device).getSensorType() == VoltageRatioSensorType.VOLTAGE_RATIO) {
						((VoltageRatioInput)device).addVoltageRatioChangeListener(new VoltageRatioInputVoltageRatioChangeListener() {
							public void onVoltageRatioChange(VoltageRatioInputVoltageRatioChangeEvent  e) {								
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
						((VoltageRatioInput)device).addSensorChangeListener(new VoltageRatioInputSensorChangeListener() {
							public void onSensorChange(VoltageRatioInputSensorChangeEvent e) {
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
		switch (deviceType) {
//		case "HIN1100": // thumbstick
		case "1046": // weatstone bridge
		case "DAQ1500": // weatstone bridge
			System.err.println("function \"read\" is not valid for device " + deviceType + ". Use \"getVoltageRatio()\" instead.\n");
			PAppletParent.exit();
			break;

		default:			
			try {
				double val =((VoltageRatioInput)device).getVoltageRatio();
				return (int)(val*1000);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get value from device " + deviceType + " because of error: " + ex);
				PAppletParent.exit();
			}
		}
		return 0; 
	}

	@Override
	public boolean getBridgeEnabled() {
		try {
			if (((VoltageRatioInput)device).getChannelSubclass() == ChannelSubclass.VOLTAGE_RATIO_INPUT_BRIDGE) { 
				return ((VoltageRatioInput)device).getBridgeEnabled();
			}
			else {
				System.err.println("getBridgeEnabled() is not valid for device of type " + deviceType);	
			}
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get BridgeEnabled value from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return false;
	}

	@Override
	public void setBridgeEnabled(boolean bridgeEnabled) {
		try {
			if (((VoltageRatioInput)device).getChannelSubclass() == ChannelSubclass.VOLTAGE_RATIO_INPUT_BRIDGE) { 
				((VoltageRatioInput)device).setBridgeEnabled(bridgeEnabled);
			}
			else {
				System.err.println("setBridgeEnabled(boolean) is not valid for device of type " + deviceType);	
			}
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set BridgeEnabled value to device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
	}

	@Override
	public int getBridgeGain() {
		try {
			if (((VoltageRatioInput)device).getChannelSubclass() == ChannelSubclass.VOLTAGE_RATIO_INPUT_BRIDGE) { 
				int g = (((VoltageRatioInput)device).getBridgeGain()).getCode();
				return 1<<(g-1);
			}
			else {
				System.err.println("getBridgeGain() is not valid for device of type " + deviceType);	
			}
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get BridgeGain value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setBridgeGain(int gain) {
		try {
			if (((VoltageRatioInput)device).getChannelSubclass() == ChannelSubclass.VOLTAGE_RATIO_INPUT_BRIDGE) { 
				if ((gain<1) || (gain>128) || ((gain&(gain-1))!=0)) {
					System.err.println("Invalid gain: " + gain + ". Use only 1, 2, 4, 8, 16, 32, 64 or 128");
					return;
				}
				BridgeGain g = BridgeGain.GAIN_1X;
				switch (gain) {
				case 1:
					break;
				case 2:
					g = BridgeGain.GAIN_2X;
					break;
				case 4:
					g = BridgeGain.GAIN_4X;
					break;
				case 8:
					g = BridgeGain.GAIN_8X;
					break;
				case 16:
					g = BridgeGain.GAIN_16X;
					break;
				case 32:
					g = BridgeGain.GAIN_32X;
					break;
				case 64:
					g = BridgeGain.GAIN_64X;
					break;
				case 128:
					g = BridgeGain.GAIN_128X;
					break;
				}
				((VoltageRatioInput)device).setBridgeGain(g);
			}
			else {
				System.err.println("setBridgeEnabled() is not valid for device of type " + deviceType);	
			}
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set BridgeEnabled value to device " + deviceType);
		}
	}

	@Override
	public int getDataInterval() {
		try {
			return ((VoltageRatioInput)device).getDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setDataInterval(int dataInterval) {
		try {
			((VoltageRatioInput)device).setDataInterval(dataInterval);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set data interval value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public int getMinDataInterval() {
		try {
			return ((VoltageRatioInput)device).getMinDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public int getMaxDataInterval() {
		try {
			return ((VoltageRatioInput)device).getMaxDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public String getSensorType() {
		try {
			VoltageRatioSensorType sensorType = ((VoltageRatioInput)device).getSensorType();
			switch (sensorType) {
			case VOLTAGE_RATIO: return "VOLTAGE_RATIO";
			case PN_1101_SHARP2D120X: return "PN_1101_SHARP2D120X";
			case PN_1101_SHARP2Y0A21: return "PN_1101_SHARP2Y0A21";
			case PN_1101_SHARP2Y0A02: return "PN_1101_SHARP2Y0A02";
			case PN_1102: return "PN_1102";
			case PN_1103: return "PN_1103";
			case PN_1104: return "PN_1104";
			case PN_1105: return "PN_1105";
			case PN_1106: return "PN_1106";
			case PN_1107: return "PN_1107";
			case PN_1108: return "PN_1108";
			case PN_1109: return "PN_1109";
			case PN_1110: return "PN_1110";
			case PN_1111: return "PN_1111";
			case PN_1112: return "PN_1112";
			case PN_1113: return "PN_1113";
			case PN_1115: return "PN_1115";
			case PN_1116: return "PN_1116";
			case PN_1118_AC: return "PN_1118_AC";
			case PN_1118_DC: return "PN_1118_DC";
			case PN_1119_AC: return "PN_1119_AC";
			case PN_1119_DC: return "PN_1119_DC";
			case PN_1120: return "PN_1120";
			case PN_1121: return "PN_1121";
			case PN_1122_AC: return "PN_1122_AC";
			case PN_1122_DC: return "PN_1122_DC";
			case PN_1124: return "PN_1124";
			case PN_1125_HUMIDITY: return "PN_1125_HUMIDITY";
			case PN_1125_TEMPERATURE: return "PN_1125_TEMPERATURE";
			case PN_1126: return "PN_1126";
			case PN_1128: return "PN_1128";
			case PN_1129: return "PN_1129";
			case PN_1131: return "PN_1131";
			case PN_1134: return "PN_1134";
			case PN_1136: return "PN_1136";
			case PN_1137: return "PN_1137";
			case PN_1138: return "PN_1138";
			case PN_1139: return "PN_1139";
			case PN_1140: return "PN_1140";
			case PN_1141: return "PN_1141";
			case PN_1146: return "PN_1146";
			case PN_3120: return "PN_3120";
			case PN_3121: return "PN_3121";
			case PN_3122: return "PN_3122";
			case PN_3123: return "PN_3123";
			case PN_3130: return "PN_3130";
			case PN_3520: return "PN_3520";
			case PN_3521: return "PN_3521";
			case PN_3522: return "PN_3522";
			}
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get sensor type from device " + deviceType + " because of error: " + ex);
		}
		return "";
	}

	@Override
	public void setSensorType(String sensorType) {
		VoltageRatioSensorType nSensorType = VoltageRatioSensorType.VOLTAGE_RATIO;

		sensorType = sensorType.toUpperCase();
		int p = sensorType.indexOf("_");
		String hSensorType = sensorType;
		if (p > 0) hSensorType = sensorType.substring(0, p);

		switch (hSensorType) {
		case "VOLTAGE": // VOLTAGE_RATIO
			break;
		case "1101":
			if (p > 0) {
				String tSensorType = sensorType.substring(p);
				switch (tSensorType) {
				case "_2D120X":
					nSensorType = VoltageRatioSensorType.PN_1101_SHARP2D120X;
					break;
				case "_2Y0A21":
					nSensorType = VoltageRatioSensorType.PN_1101_SHARP2Y0A21;
					break;
				case "_2Y0A02":
					nSensorType = VoltageRatioSensorType.PN_1101_SHARP2Y0A02;
					break;
				default:
					System.out.println("For using sensor value with Sharp proximity sensors, use specific sensor name. e.g. \"2D120X\"");
					break;
				}
			}
			else {
				System.out.println("For using sensor value with Sharp proximity sensors, use specific sensor name. e.g. \"2D120X\"");
			}
			return;
		case "SHARP2D120X":
		case "2D120X":
		case "3520":
			nSensorType = VoltageRatioSensorType.PN_1101_SHARP2D120X;
			break;
		case "SHARP2Y0A21":
		case "2Y0A21":
		case "3521":
			nSensorType = VoltageRatioSensorType.PN_1101_SHARP2Y0A21;
			break;
		case "SHARP2Y0A02":
		case "2Y0A02":
		case "3522":
			nSensorType = VoltageRatioSensorType.PN_1101_SHARP2Y0A02;
			break;
		case "1102":
			nSensorType = VoltageRatioSensorType.PN_1102;
			break;
		case "1103":
			nSensorType = VoltageRatioSensorType.PN_1103;
			break;
		case "1104":
			nSensorType = VoltageRatioSensorType.PN_1104;
			break;
		case "1105":
			nSensorType = VoltageRatioSensorType.PN_1105;
			break;
		case "1106":
			nSensorType = VoltageRatioSensorType.PN_1106;
			break;
		case "1107":
			nSensorType = VoltageRatioSensorType.PN_1107;
			break;
		case "1108":
			nSensorType = VoltageRatioSensorType.PN_1108;
			break;
		case "1109":
			nSensorType = VoltageRatioSensorType.PN_1109;
			break;
		case "1110":
			nSensorType = VoltageRatioSensorType.PN_1110;
			break;
		case "1111":
			nSensorType = VoltageRatioSensorType.PN_1111;
			break;
		case "1112":
			nSensorType = VoltageRatioSensorType.PN_1112;
			break;
		case "1113":
			nSensorType = VoltageRatioSensorType.PN_1113;
			break;
		case "1115":
			nSensorType = VoltageRatioSensorType.PN_1115;
			break;
		case "1116":
			nSensorType = VoltageRatioSensorType.PN_1116;
			break;
		case "1118":
			if (p > 0) {
				String tSensorType = sensorType.substring(p);
				switch (tSensorType) {
				case "_AC":
					nSensorType = VoltageRatioSensorType.PN_1118_AC;
					break;
				case "_DC":
					nSensorType = VoltageRatioSensorType.PN_1118_DC;
					break;
				default:	
					System.out.println("For using sensor value with a current sensor, specify if you connect to DC or AC current. e.g. \"1118AC\"");
					break;
				}
			}
			else {
				System.out.println("For using sensor value with a current sensor, specify if you connect to DC or AC current. e.g. \"1118AC\"");
			}
			return;
		case "1118AC":
			nSensorType = VoltageRatioSensorType.PN_1118_AC;
			break;
		case "1118DC":
			nSensorType = VoltageRatioSensorType.PN_1118_DC;
			break;
		case "1119":
			if (p > 0) {
				String tSensorType = sensorType.substring(p);
				switch (tSensorType) {
				case "_AC":
					nSensorType = VoltageRatioSensorType.PN_1119_AC;
					break;
				case "_DC":
					nSensorType = VoltageRatioSensorType.PN_1119_DC;
					break;
				default:	
					System.out.println("For using sensor value with a current sensor, specify if you connect to DC or AC current. e.g. \"1119AC\"");
					break;
				}
			}
			else {
				System.out.println("For using sensor value with a current sensor, specify if you connect to DC or AC current. e.g. \"1119AC\"");
			}
			return;
		case "1119AC":
			nSensorType = VoltageRatioSensorType.PN_1119_AC;
			break;
		case "1119DC":
			nSensorType = VoltageRatioSensorType.PN_1119_DC;
			break;
		case "1120":
			nSensorType = VoltageRatioSensorType.PN_1120;
			break;
		case "1121":
			nSensorType = VoltageRatioSensorType.PN_1121;
			break;
		case "1122":
			if (p > 0) {
				String tSensorType = sensorType.substring(p);
				switch (tSensorType) {
				case "_AC":
					nSensorType = VoltageRatioSensorType.PN_1122_AC;
					break;
				case "_DC":
					nSensorType = VoltageRatioSensorType.PN_1122_DC;
					break;
				default:	
					System.out.println("For using sensor value with a current sensor, specify if you connect to DC or AC current. e.g. \"1122AC\"");
					break;
				}
			}
			else {
				System.out.println("For using sensor value with a current sensor, specify if you connect to DC or AC current. e.g. \"1122AC\"");
			}
			return;
		case "1122AC":
			nSensorType = VoltageRatioSensorType.PN_1122_AC;
			break;
		case "1122DC":
			nSensorType = VoltageRatioSensorType.PN_1122_DC;
			break;
		case "1124":
			nSensorType = VoltageRatioSensorType.PN_1124;
			break;
		case "1125":
			if (p > 0) {
				String tSensorType = sensorType.substring(p);
				switch (tSensorType) {
				case "_HUMIDITY":
					nSensorType = VoltageRatioSensorType.PN_1125_HUMIDITY;
					break;
				case "_TEMPERATURE":
					nSensorType = VoltageRatioSensorType.PN_1125_TEMPERATURE;
					break;
				default:	
					System.out.println("For using sensor value with 1125 sensor, specify if you measure humidity or temperature. e.g. \"1125_TEMPERATURE\"");
					break;
				}
			}
			else {
				System.out.println("For using sensor value with 1125 sensor, specify if you measure humidity or temperature. e.g. \"1125_TEMPERATURE\"");
			}
			return;
		case "1125HUMIDITY":
		case "HUMIDITY":
			nSensorType = VoltageRatioSensorType.PN_1125_HUMIDITY;
			break;
		case "1125TEMPERATURE":
		case "TEMPERATURE":
			nSensorType = VoltageRatioSensorType.PN_1125_TEMPERATURE;
			break;
		case "1126":
			nSensorType = VoltageRatioSensorType.PN_1126;
			break;
		case "1128":
			nSensorType = VoltageRatioSensorType.PN_1128;
			break;
		case "1129":
			nSensorType = VoltageRatioSensorType.PN_1129;
			break;
		case "1131":
			nSensorType = VoltageRatioSensorType.PN_1131;
			break;
		case "1134":
			nSensorType = VoltageRatioSensorType.PN_1134;
			break;
		case "1136":
			nSensorType = VoltageRatioSensorType.PN_1136;
			break;
		case "1137":
			nSensorType = VoltageRatioSensorType.PN_1137;
			break;
		case "1138":
			nSensorType = VoltageRatioSensorType.PN_1138;
			break;
		case "1139":
			nSensorType = VoltageRatioSensorType.PN_1139;
			break;
		case "1140":
			nSensorType = VoltageRatioSensorType.PN_1140;
			break;
		case "1141":
			nSensorType = VoltageRatioSensorType.PN_1141;
			break;
		case "1146":
			nSensorType = VoltageRatioSensorType.PN_1146;
			break;
		case "3120":
			nSensorType = VoltageRatioSensorType.PN_3120;
			break;
		case "3121":
			nSensorType = VoltageRatioSensorType.PN_3121;
			break;
		case "3122":
			nSensorType = VoltageRatioSensorType.PN_3122;
			break;
		case "3123":
			nSensorType = VoltageRatioSensorType.PN_3123;
			break;
		case "3130":
			nSensorType = VoltageRatioSensorType.PN_3130;
			break;
		case "AC": // in case the intialization was done without specific current type
			switch (deviceType) {
			case "1118":
				nSensorType = VoltageRatioSensorType.PN_1118_AC;
				break;
			case "1119":
				nSensorType = VoltageRatioSensorType.PN_1119_AC;
				break;
			case "1122":
				nSensorType = VoltageRatioSensorType.PN_1122_AC;
				break;
			default:
				System.err.println("Cannot set sensor of type " + deviceType + " to AC");		
				return;
			}
			break;
		case "DC": // in case the intialization was done without specific current type
			switch (deviceType) {
			case "1118":
				nSensorType = VoltageRatioSensorType.PN_1118_DC;
				break;
			case "1119":
				nSensorType = VoltageRatioSensorType.PN_1119_DC;
				break;
			case "1122":
				nSensorType = VoltageRatioSensorType.PN_1122_DC;
				break;
			default:
				System.err.println("Cannot set sensor of type " + deviceType + " to DC");		
				return;
			}
			break;
		default:
			System.err.println("Cannot set sensor type to " + sensorType +". See documentation for correct usage");		
			return;
		}
		try {
			((VoltageRatioInput)device).setSensorType(nSensorType);
			attachListeners();
			// if not in setup() and real-time event is registered - run the pre() function to re-create it if needed
			if (RTEventRegister && (PAppletParent.frameCount > 0)) pre();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set sensor type for device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public String getSensorUnit() {
		try {
			if (((VoltageRatioInput)device).getSensorType() == VoltageRatioSensorType.VOLTAGE_RATIO) {
				System.err.println("Cannot get sensor unit for device " + deviceType + " because sensor type not set. use \"setSensorType\" inside setup().");
				return "";
			}
			return (((VoltageRatioInput)device).getSensorUnit().name);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get sensor unit for device " + deviceType + " because of error: " + ex);
		}
		return "";
	}

	@Override
	public float getSensorValue() {
		try {
			if (((VoltageRatioInput)device).getSensorType() == VoltageRatioSensorType.VOLTAGE_RATIO) {
				System.err.println("Cannot get sensor value for device " + deviceType + " because sensor type not set. use \"setSensorType\" inside setup().");
				return 0.0f;
			}
			return (float)(((VoltageRatioInput)device).getSensorValue());
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
		return 0.0f;
	}

	@Override
	public boolean getSensorValueValidity() {
		try {
			if (((VoltageRatioInput)device).getSensorType() == VoltageRatioSensorType.VOLTAGE_RATIO) {
				System.err.println("Cannot check sensor value validity for device " + deviceType + " because sensor type not set. use \"setSensorType\" inside setup().");
				return false;
			}
			@SuppressWarnings("unused")
			double reading = ((VoltageRatioInput)device).getSensorValue();
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
	public float getSensorValueChangeTrigger() {
		try {
			return (float)(((VoltageRatioInput)device).getSensorValueChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get sensor value change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public void setSensorValueChangeTrigger(float sensorValueChangeTrigger) {
		try {
			((VoltageRatioInput)device).setSensorValueChangeTrigger((double)sensorValueChangeTrigger);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set sensor value change trigger for device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getVoltageRatio() {
		try {
			return (float)(((VoltageRatioInput)device).getVoltageRatio());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get voltage ratio for device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0.0f;
	}

	@Override
	public float getMinVoltageRatio() {
		try {
			return (float)(((VoltageRatioInput)device).getMinVoltageRatio());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min voltage ratio for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public float getMaxVoltageRatio() {
		try {
			return (float)(((VoltageRatioInput)device).getMaxVoltageRatio());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max voltage ratio for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public float getVoltageRatioChangeTrigger() {
		try {
			return (float)(((VoltageRatioInput)device).getVoltageRatioChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get voltage ratio change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public void setVoltageRatioChangeTrigger(float voltageRatioChangeTrigger) {
		try {
			((VoltageRatioInput)device).setVoltageRatioChangeTrigger((double)voltageRatioChangeTrigger);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set voltage ratio change trigger for device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public void setReadChangeTrigger(int readChangeTrigger) {
		try {
			((VoltageRatioInput)device).setVoltageRatioChangeTrigger(((double)readChangeTrigger)/1000.0);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set read change trigger for device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getMinVoltageRatioChangeTrigger() {
		try {
			return (float)(((VoltageRatioInput)device).getMinVoltageRatioChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min voltage ratio change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public float getMaxVoltageRatioChangeTrigger() {
		try {
			return (float)(((VoltageRatioInput)device).getMaxVoltageRatioChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max voltage ratio change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}
}
