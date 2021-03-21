package shenkar.SimplePhidgets;

/* ToDos:
 * - Check if possible to open both DCMotor and MotorPositionController at the same time for one device.
 * - Check where specific function screening for specific devices can be done using getChannelSubClass
 * - document MOT2002 as unique - defaultly checked as medium sensitivity, but can be changed
 * - spatial: check quaternion update with applicable device (probably only 1044_1)
 * - digital: expand secondaryIO to secondaryIO, or change scheme, for example for 1065 motor controller, which has motor control + analog + encoder + digital...
 */

import processing.core.*;
//import sun.text.normalizer.UnicodeSet.SpanCondition;

//import java.io.*;
//import com.phidget22.*;

/**
 * The Channel class is a general container for a phidget channel. It is used for any control of a single channel - both inputs and outputs.
 * For using sensors, the user can use the simplified function "read" and get a value from 0 to 1000 according to sensor reading, or
 *  using sensor's other functions according to phidget's reference.
 * For most actuators, the simplified functions of "on" and "off" are implemented, or using other Phidget's functions.
 * Many initialization methods are possible, depending on system setup. See github.com/sgeigers/SimplePhidgets#reference for details
 * 
 * @example Basic/Digital_Input
 * @example Basic/Servo_Motor_Basic
 * @example Basic/Simple_Sensor
 * @example Specific/Distance_Sharp_Sensor_1101
 * @example Specific/Spatial
 * @example Specific/HIN1100_Thumbstick
 * @example Advanced/Digital_Input_Event
 * @example Advanced/Multiple_Hubs
 * @example Advanced/Multiple_Sensors_Events
 * @example Advanced/Simple_Sensor_Event
 */

public class Channel {

	public final static String VERSION = "1.0.0";

	// myParent is a reference to the parent sketch
	PApplet myParent;
	String deviceType;
	Device device;

	// P_Voltage_Ratio, 
	/**
	 * most basic use for sensors. returns an integer between 0 an 1000 coresponding to reading of the sensor.
	 * 
	 * @return int
	 */
	public int read() {return device.read(); } // returns sensor value, between 0 and 1000

	// P_Voltage_Ratio
	public boolean getBridgeEnabled() {return device.getBridgeEnabled(); }
	public void setBridgeEnabled(boolean bridgeEnabled) {device.setBridgeEnabled(bridgeEnabled); }
	public int getBridgeGain() {return device.getBridgeGain(); } // 1, 2, 4, 8, 16, 32, 64, 128
	public void setBridgeGain(int gain) {device.setBridgeGain(gain); } // 1, 2, 4, 8, 16, 32, 64, 128

	// P_Voltage_Ratio, P_Voltage_Input, P_Sound_Sensor, P_Capacitive_Touch, P_Spatial, P_RC_Servo
	public int getDataInterval() {return device.getDataInterval(); }
	public void setDataInterval(int dataInterval) {device.setDataInterval(dataInterval); }
	public int getMinDataInterval() {return device.getMinDataInterval(); }  // milliseconds
	public int getMaxDataInterval() {return device.getMaxDataInterval(); }  // milliseconds

	// P_Voltage_Ratio, P_Voltage_Input, P_Sound_Sensor, P_Capacitive_Touch
	public void setReadChangeTrigger(int readChangeTrigger) {device.setReadChangeTrigger(readChangeTrigger); }

	// P_Voltage_Ratio, P_Voltage_Input	
	public String getSensorType() {return device.getSensorType(); }
	public void setSensorType(String sensorType) {device.setSensorType(sensorType); }
	public String getSensorUnit() {return device.getSensorUnit(); }
	public float getSensorValue() {return device.getSensorValue(); }
	public boolean getSensorValueValidity() {return device.getSensorValueValidity(); }  // to avoid getting an error every time the reading is outside scale (like when connecting Sharp proximity sensors)
	public float getSensorValueChangeTrigger() {return device.getSensorValueChangeTrigger(); }
	public void setSensorValueChangeTrigger(float sensorValueChangeTrigger) {device.setSensorValueChangeTrigger(sensorValueChangeTrigger); }

	// P_Voltage_Ratio	
	public float getVoltageRatio() {return device.getVoltageRatio(); }
	public float getMinVoltageRatio() {return device.getMinVoltageRatio(); }
	public float getMaxVoltageRatio() {return device.getMaxVoltageRatio(); }
	public float getVoltageRatioChangeTrigger() {return device.getVoltageRatioChangeTrigger(); }
	public void setVoltageRatioChangeTrigger(float voltageRatioChangeTrigger) {device.setVoltageRatioChangeTrigger(voltageRatioChangeTrigger); }
	public float getMinVoltageRatioChangeTrigger() {return device.getMinVoltageRatioChangeTrigger(); }
	public float getMaxVoltageRatioChangeTrigger() {return device.getMaxVoltageRatioChangeTrigger(); }

	// P_Voltage_Input, P_Digital_Input
	public int getPowerSupply() { return device.getPowerSupply(); }
	public void setPowerSupply(int gain) {device.setPowerSupply(gain); } // 0, 12, 24

	// P_Voltage_Input
	public float getVoltage() { return device.getVoltage(); }
	public float getMinVoltage() { return device.getMinVoltage(); }
	public float getMaxVoltage() { return device.getMaxVoltage(); }
	public float getVoltageChangeTrigger() { return device.getVoltageChangeTrigger(); }
	public void setVoltageChangeTrigger(float voltageChangeTrigger) { device.setVoltageChangeTrigger(voltageChangeTrigger); }
	public float getMinVoltageChangeTrigger() { return device.getMinVoltageChangeTrigger(); }
	public float getMaxVoltageChangeTrigger() { return device.getMaxVoltageChangeTrigger(); }
	public int getVoltageRange() { return device.getVoltageRange(); } //  millivolts: 10, 40, 200, 312 (for 312.5mV), 400, 1000, 2000, 5000, 15000, 40000 or -1 for AUTO
	public void setVoltageRange(int vr) { device.setVoltageRange(vr); } //  millivolts: 10, 40, 200, 312 (for 312.5mV), 400, 1000, 2000, 5000, 15000, 40000 or -1 for AUTO

	// P_Sound_Sensor
	public float getdB() {return device.getdB(); }
	public float getMaxdB() {return device.getMaxdB(); }
	public float getdBA() {return device.getdBA(); }
	public float getdBC() {return device.getdBC(); }
	public float getNoiseFloor() {return device.getNoiseFloor(); }
	public float[] getOctaves() {return device.getOctaves(); }
	public float getSPLChangeTrigger() {return device.getSPLChangeTrigger(); }
	public void setSPLChangeTrigger(float SPLChangeTrigger) { device.setSPLChangeTrigger(SPLChangeTrigger); }
	public float getMinSPLChangeTrigger() {return device.getMinSPLChangeTrigger(); }
	public float getMaxSPLChangeTrigger() {return device.getMaxSPLChangeTrigger(); }
	public int getSPLRange() { return device.getSPLRange(); }
	public void setSPLRange(int range) {device.setSPLRange(range); }

	// P_Capacitive_Touch
	public boolean getIsTouched() {return device.getIsTouched(); }
	public float getSensitivity() {return device.getSensitivity(); }
	public void setSensitivity(float sensitivity) {device.setSensitivity(sensitivity); }
	public float getMinSensitivity() {return device.getMinSensitivity(); }
	public float getMaxSensitivity() {return device.getMaxSensitivity(); }
	public float getTouchValue() {return device.getTouchValue(); }
	public float getMinTouchValue() {return device.getMinTouchValue(); }
	public float getMaxTouchValue() {return device.getMaxTouchValue(); }
	public float getTouchValueChangeTrigger() {return device.getTouchValueChangeTrigger(); }
	public void setTouchValueChangeTrigger(float touchValueChangeTrigger) {device.setTouchValueChangeTrigger(touchValueChangeTrigger); }
	public float getMinTouchValueChangeTrigger() {return device.getMinTouchValueChangeTrigger(); }
	public float getMaxTouchValueChangeTrigger() {return device.getMaxTouchValueChangeTrigger(); }

	// P_Spatial
	public float getRoll() {return device.getRoll(); }
	public float getPitch() {return device.getPitch(); }
	public float getYaw() {return device.getYaw(); }
	public float[] getQuaternion() {return device.getQuaternion(); }
	public float[] getAccelerationArray() {return device.getAccelerationArray(); }
	public float[] getMinAccelerationArray() {return device.getMinAccelerationArray(); }
	public float[] getMaxAccelerationArray() {return device.getMaxAccelerationArray(); }
	public float[] getAngularRate() {return device.getAngularRate(); }
	public float[] getMinAngularRate() {return device.getMinAngularRate(); }
	public float[] getMaxAngularRate() {return device.getMaxAngularRate(); }
	public float[] getMagneticField() {return device.getMagneticField(); }
	public float[] getMinMagneticField() {return device.getMinMagneticField(); }
	public float[] getMaxMagneticField() {return device.getMaxMagneticField(); }
	public float getAccelerationChangeTrigger() {return device.getAccelerationChangeTrigger(); }
	public void setAccelerationChangeTrigger(float accelerationChangeTrigger) {device.setAccelerationChangeTrigger(accelerationChangeTrigger); }
	public float getMinAccelerationChangeTrigger() {return device.getMinAccelerationChangeTrigger(); }
	public float getMaxAccelerationChangeTrigger() {return device.getMaxAccelerationChangeTrigger(); }
	public float getMagneticFieldChangeTrigger() {return device.getMagneticFieldChangeTrigger(); }
	public void setMagneticFieldChangeTrigger(float magneticFieldChangeTrigger) {device.setMagneticFieldChangeTrigger(magneticFieldChangeTrigger); }
	public float getMinMagneticFieldChangeTrigger() {return device.getMinMagneticFieldChangeTrigger(); }
	public float getMaxMagneticFieldChangeTrigger() {return device.getMaxMagneticFieldChangeTrigger(); }
	public int getAxisCount() {return device.getAxisCount(); }
	public float getTimestamp() {return device.getTimestamp(); }
	public void zeroGyro() {device.zeroGyro(); }
	public void setMagnetometerCorrectionParameters(float magneticField, float offset0,	float offset1, float offset2, float gain0, float gain1, float gain2, float T0, float T1, float T2, float T3, float T4, float T5) 
	{device.setMagnetometerCorrectionParameters(magneticField, offset0,	offset1, offset2, gain0, gain1, gain2, T0, T1, T2, T3, T4, T5); }
	public void resetMagnetometerCorrectionParameters() {device.resetMagnetometerCorrectionParameters(); }
	public void saveMagnetometerCorrectionParameters() {device.saveMagnetometerCorrectionParameters(); }
	public String getAlgorithm() {return device.getAlgorithm(); }
	public void zeroAlgorithm() {device.zeroAlgorithm(); }
	public void setAlgorithm(String alg) {device.setAlgorithm(alg); }

	// P_Digital_Input
	public String getInputMode() {return device.getInputMode(); }
	public void setInputMode(String im) {device.setInputMode(im); }
	public boolean getState() {return device.getState(); }
	
	// P_Digital_Output
	public void on() {device.on(); }
	public void off() {device.off(); }
	public void analogWrite(int dutyCycle) {device.analogWrite(dutyCycle); }
	public float getDutyCycle() {return device.getDutyCycle(); }
	public void setDutyCycle(float dutyCycle) {device.setDutyCycle(dutyCycle); }
	public float getMinDutyCycle() {return device.getMinDutyCycle(); }
	public float getMaxDutyCycle() {return device.getMaxDutyCycle(); }
	public float getFrequency() {return device.getFrequency(); }
	public void setFrequency(float frequency) {device.setFrequency(frequency); }
	public float getMinFrequency() {return device.getMinFrequency(); }
	public float getMaxFrequency() {return device.getMaxFrequency(); }
	public float getLEDCurrentLimit() {return device.getLEDCurrentLimit(); }
	public void setLEDCurrentLimit(float LEDCurrentLimit) {device.setLEDCurrentLimit(LEDCurrentLimit); }
	public float getMinLEDCurrentLimit() {return device.getMinLEDCurrentLimit(); }
	public float getMaxLEDCurrentLimit() {return device.getMaxLEDCurrentLimit(); }
	public String getLEDForwardVoltage() {return device.getLEDForwardVoltage(); }
	public void setLEDForwardVoltage(String LEDFV) {device.setLEDForwardVoltage(LEDFV); }

	// P_Digital_Output, P_RC_Servo
	public void enableFailsafe(int failsafeTime) {device.enableFailsafe(failsafeTime); }	
	public int getMinFailsafeTime() {return device.getMinFailsafeTime(); }
	public int getMaxFailsafeTime() {return device.getMaxFailsafeTime(); }
	public void resetFailsafe() {device.resetFailsafe(); }	

	// P_RC_SERVO
	public void setAngle(int ang) {device.setAngle(ang); }
	public void setAngle(float ang) {device.setAngle(ang); }
	public float getAcceleration() {return device.getAcceleration(); }
	public void setAcceleration(float accel) {device.setAcceleration(accel); }
	public float getMinAcceleration() {return device.getMinAcceleration(); }
	public float getMaxAcceleration() {return device.getMaxAcceleration(); }
	public boolean getEngaged() {return device.getEngaged(); }
	public void setEngaged(boolean eng) {device.setEngaged(eng); }
	public boolean getIsMoving() {return device.getIsMoving(); }
	public float getPosition() {return device.getPosition(); }
	public void setMinPosition(float pos) {device.setMinPosition(pos); }
	public float getMinPosition() {return device.getMinPosition(); }
	public void setMaxPosition(float pos) {device.setMaxPosition(pos); }
	public float getMaxPosition() {return device.getMaxPosition(); }
	public void setMinPulseWidth(float pls) {device.setMinPulseWidth(pls); }
	public float getMinPulseWidth() {return device.getMinPulseWidth(); }
	public void setMaxPulseWidth(float pls) {device.setMaxPulseWidth(pls); }
	public float getMaxPulseWidth() {return device.getMaxPulseWidth(); }
	public float getMinPulseWidthLimit() {return device.getMinPulseWidthLimit(); }
	public float getMaxPulseWidthLimit() {return device.getMaxPulseWidthLimit(); }
	public boolean getSpeedRampingState() {return device.getSpeedRampingState(); }
	public void setSpeedRampingState(boolean state) {device.setSpeedRampingState(state); }
	public float getTargetPosition() {return device.getTargetPosition(); }
	public void setTargetPosition(float tgt) {device.setTargetPosition(tgt); }
	public float getTorque() {return device.getTorque(); }
	public void setTorque(float trq) {device.setTorque(trq); }
	public float getMinTorque() {return device.getMinTorque(); }
	public float getMaxTorque() {return device.getMaxTorque(); }
	public float getVelocity() {return device.getVelocity(); }
	public float getVelocityLimit() {return device.getVelocityLimit(); }
	public void setVelocityLimit(float vel) {device.setVelocityLimit(vel); }
	public float getMinVelocityLimit() {return device.getMinVelocityLimit(); }
	public float getMaxVelocityLimit() {return device.getMaxVelocityLimit(); }
	public String getVoltageString() {return device.getVoltageString(); }
	public void setVoltage(String vol) {device.setVoltage(vol); }
	
	/**
	 * minimal constructor
	 * 
	 * @param theParent the parent PApplet
	 * @param type 4 numbers or 3 letters and 4 numbers describing type of device to associate to this channel
	 */
	public Channel(PApplet theParent, String type) {
		this(theParent, type, -1, 0, -1, "");
	}

	/**
	 * broader constructor
	 * 
	 * @param theParent the parent PApplet
	 * @param type 4 numbers or 3 letters and 4 numbers describing type of device to associate to this channel
	 * @param hubPort the port number of hub or interfaceKit where the device is connected, or hub/IK serial number (if number used has more than 4 digits)
	 */
	public Channel(PApplet theParent, String type, int hubPort) {
		this(theParent, type, -1, hubPort, -1, "");
	}

	/**
	 * broaderer constructor
	 * 
	 * @param theParent the parent PApplet
	 * @param type 4 numbers or 3 letters and 4 numbers describing type of device to associate to this channel
	 * @param hubPort the port number of hub or interfaceKit where the device is connected, or hub/IK serial number (if number used has more than 4 digits)
	 * @param chNum specify the channel used (see general constructor), or hub port if serial number was specified in previous parameter
	 */
	public Channel(PApplet theParent, String type, int hubPort, int chNum) {
		this(theParent, type, -1, hubPort, chNum, "");
	}

	/**
	 * minimal constructor w/ secondary I/O
	 * 
	 * @param theParent the parent PApplet
	 * @param type 4 numbers or 3 letters and 4 numbers describing type of device to associate to this channel
	 * @param secondaryIO either "digital_input" or "digital_output" to indicate digital channel (unless trivial by board type)
	 */
	public Channel(PApplet theParent, String type, String secondaryIO) {
		this(theParent, type, -1, 0, -1, secondaryIO);
	}

	/**
	 * broader constructor w/ secondary I/O
	 * 
	 * @param theParent the parent PApplet
	 * @param type 4 numbers or 3 letters and 4 numbers describing type of device to associate to this channel
	 * @param hubPort the port number of hub or interfaceKit where the device is connected, or hub/IK serial number (if number used has more than 4 digits)
	 * @param secondaryIO either "digital_input" or "digital_output" to indicate digital channel (unless trivial by board type)
	 */
	public Channel(PApplet theParent, String type, int hubPort, String secondaryIO) {
		this(theParent, type, -1, hubPort, -1, secondaryIO);
	}

	/**
	 * broader constructor 2 w/ secondary I/O
	 * 
	 * @param theParent the parent PApplet
	 * @param type 4 numbers or 3 letters and 4 numbers describing type of device to associate to this channel
	 * @param secondaryIO either "digital_input" or "digital_output" to indicate digital channel (unless trivial by board type)
	 * @param hubPort the port number of hub or interfaceKit where the device is connected, or hub/IK serial number (if number used has more than 4 digits)
	 */
	public Channel(PApplet theParent, String type, String secondaryIO, int hubPort) {
		this(theParent, type, -1, hubPort, -1, secondaryIO);
	}

	/**
	 * broaderer constructor w/ secondary I/O
	 * 
	 * @param theParent the parent PApplet
	 * @param type 4 numbers or 3 letters and 4 numbers describing type of device to associate to this channel
	 * @param hubPort the port number of hub or interfaceKit where the device is connected, or hub/IK serial number (if number used has more than 4 digits)
	 * @param chNum specify the channel used (see general constructor), or hub port if serial number was specified in previous parameter
	 * @param secondaryIO either "digital_input" or "digital_output" to indicate digital channel (unless trivial by board type)
	 */
	public Channel(PApplet theParent, String type, int hubPort, int chNum, String secondaryIO) {
		this(theParent, type, -1, hubPort, chNum, secondaryIO);
	}

	/**
	 * broaderer constructor 2 w/ secondary I/O
	 * 
	 * @param theParent the parent PApplet
	 * @param type 4 numbers or 3 letters and 4 numbers describing type of device to associate to this channel
	 * @param hubPort the port number of hub or interfaceKit where the device is connected, or hub/IK serial number (if number used has more than 4 digits)
	 * @param secondaryIO either "digital_input" or "digital_output" to indicate digital channel (unless trivial by board type)
	 * @param chNum specify the channel used (see general constructor), or hub port if serial number was specified in previous parameter
	 */
	public Channel(PApplet theParent, String type, int hubPort, String secondaryIO, int chNum) {
		this(theParent, type, -1, hubPort, chNum, secondaryIO);
	}

	/**
	 * broaderer constructor 3 w/ secondary I/O
	 * 
	 * @param theParent the parent PApplet
	 * @param type 4 numbers or 3 letters and 4 numbers describing type of device to associate to this channel
	 * @param secondaryIO either "digital_input" or "digital_output" to indicate digital channel (unless trivial by board type)
	 * @param hubPort the port number of hub or interfaceKit where the device is connected, or hub/IK serial number (if number used has more than 4 digits)
	 * @param chNum specify the channel used (see general constructor), or hub port if serial number was specified in previous parameter
	 */
	public Channel(PApplet theParent, String type, String secondaryIO, int hubPort, int chNum) {
		this(theParent, type, -1, hubPort, chNum, secondaryIO);
	}

	/**
	 *  the most general constructor w/ secondary I/O 
	 * 
	 * @param theParent the parent PApplet
	 * @param type 4 numbers or 3 letters and 4 numbers describing type of device to associate to this channel
	 * @param serial serial number of hub or interfaceKit (to be used in case there are more than one hub/IK connected to the computer)
	 * @param hubPort the port number of hub or interfaceKit where the device is connected, or hub/IK serial number (if number used has more than 4 digits)
	 * @param secondaryIO either "digital_input" or "digital_output" to indicate digital channel (unless trivial by board type)
	 * @param chNum specify the channel used (see general constructor), or hub port if serial number was specified in previous parameter
	 */
	public Channel(PApplet theParent, String type, int serialNum, int hubPort, String secondaryIO, int chNum) {
		this(theParent, type, -1, hubPort, chNum, secondaryIO);
	}

	/**
	 *  the most general constructor w/ secondary I/O 2
	 * 
	 * @param theParent the parent PApplet
	 * @param type 4 numbers or 3 letters and 4 numbers describing type of device to associate to this channel
	 * @param serial serial number of hub or interfaceKit (to be used in case there are more than one hub/IK connected to the computer)
	 * @param secondaryIO either "digital_input" or "digital_output" to indicate digital channel (unless trivial by board type)
	 * @param hubPort the port number of hub or interfaceKit where the device is connected, or hub/IK serial number (if number used has more than 4 digits)
	 * @param chNum specify the channel used (see general constructor), or hub port if serial number was specified in previous parameter
	 */
	public Channel(PApplet theParent, String type, int serialNum, String secondaryIO, int hubPort, int chNum) {
		this(theParent, type, -1, hubPort, chNum, secondaryIO);
	}

	/**
	 *  the most general constructor w/ secondary I/O 3
	 * 
	 * @param theParent the parent PApplet
	 * @param type 4 numbers or 3 letters and 4 numbers describing type of device to associate to this channel
	 * @param secondaryIO either "digital_input" or "digital_output" to indicate digital channel (unless trivial by board type)
	 * @param serial serial number of hub or interfaceKit (to be used in case there are more than one hub/IK connected to the computer)
	 * @param hubPort the port number of hub or interfaceKit where the device is connected, or hub/IK serial number (if number used has more than 4 digits)
	 * @param chNum specify the channel used (see general constructor), or hub port if serial number was specified in previous parameter
	 */
	public Channel(PApplet theParent, String type, String secondaryIO, int serialNum, int hubPort, int chNum) {
		this(theParent, type, -1, hubPort, chNum, secondaryIO);
	}



	/**
	 * the most general constructor w/ secondary I/O 4
	 * 
	 * @param theParent the parent PApplet
	 * @param type 4 numbers or 3 letters and 4 numbers describing type of device to associate to this channel
	 * @param serial serial number of hub or interfaceKit (to be used in case there are more than one hub/IK connected to the computer)
	 * @param hubPort the port number of hub or interfaceKit where the device is connected. Only needed if differnt than 0 or if a channel number is specified
	 * @param chNum if the device has more than one channel (e.g. wheatstone bridge) - specify the channel used
	 */
	public Channel(PApplet theParent, String type, int serialNum, int hubPort, int chNum, String secondaryIO) {
		myParent = theParent;
		myParent.registerMethod("draw", this);
		myParent.registerMethod("dispose", this);
		if (hubPort > 9999) {
			serialNum = hubPort;
			if (chNum>-1) hubPort = chNum;
			else hubPort = 0;
			chNum = -1;
		}

		deviceType = type;
		int p = deviceType.indexOf("_");
		if (p > 0) deviceType = deviceType.substring(0, p);
		deviceType = deviceType.toUpperCase();

		// if secondary I/O isn't empty, we handle it first:
		if (secondaryIO != "") {
			secondaryIO = secondaryIO.toUpperCase();
			switch (secondaryIO) {
			case "DIGITALINPUT":
				switch (deviceType) {
				case "1010": // PhidgetInterfaceKit 8/8/8
				case "1011": // PhidgetInterfaceKit 2/2/2
				case "1012": // PhidgetInterfaceKit 0/16/16
				case "1013": // PhidgetInterfaceKit 8/8/8
				case "1018": // PhidgetInterfaceKit 8/8/8
				case "1019": // PhidgetInterfaceKit 8/8/8
				case "1047": // PhidgetEncoder HighSpeed 4-Input 
				case "1052": // PhidgetEncoder
				case "1060": // PhidgetMotorControl LV
				case "1063": // PhidgetStepper Bipolar 1-Motor
				case "1065": // PhidgetMotorControl 1-Motor
				case "1070": // PhidgetSBC
				case "1072": // PhidgetSBC2
				case "1073": // PhidgetSBC3
				case "1202": // PhidgetTextLCD 20X2 : Blue : Integrated PhidgetInterfaceKit 8/8/8
				case "1203": // PhidgetTextLCD 20X2 : White : Integrated PhidgetInterfaceKit 8/8/8
				case "1219": // PhidgetTextLCD 20X2 White with PhidgetInterfaceKit 0/8/8
				case "1220": // PhidgetTextLCD 20X2 Blue with PhidgetInterfaceKit 0/8/8
				case "1221": // PhidgetTextLCD 20X2 Green with PhidgetInterfaceKit 0/8/8
				case "1222": // PhidgetTextLCD 20X2 Red with PhidgetInterfaceKit 0/8/8
				case "DAQ1200": // 4x Digital Input Phidget
				case "DAQ1300": // 4x Isolated Digital Input Phidget
				case "DAQ1301": // 16x Isolated Digital Input Phidget
				case "DAQ1400": // Versatile Input Phidget
				case "HIN1100": // Thumbstick Phidget
				case "HIN1101": // Dial Phidget
				case "HUB0000": // 6-Port USB VINT Hub Phidget
				case "HUB5000": // 6-Port Network VINT Hub Phidget
				case "SBC3003": // PhidgetSBC4 - 6-Port VINT Hub Phidget
					device = new P_Digital_Input(myParent, this, deviceType, serialNum, hubPort, chNum);
					break;

				default:
					System.out.println("device " + deviceType + " has no secondary I/O of type \"digitalInput\"");	
					break;
				}
				break;

			case "ANALOGINPUT":
				switch (deviceType) {
/*					case "HIN1100": // Thumbstick Phidget
						device = new P_Digital_Input(myParent, this, deviceType, serialNum, hubPort, chNum);
						break;
*/						
					default:
						System.out.println("device " + deviceType + " has no secondary I/O of type \"analogInput\"");	
						break;
				}
				break;

			case "VOLTAGEINPUT":
				switch (deviceType) {
					case "1051": // PhidgetTemperatureSensor 1-Input
					case "1058": // PhidgetPhSensor
					case "ADP1000": // pH Phidget
					case "SAF1000": // Programmable Power Guard Phidget
					case "TMP1100": // Isolated Thermocouple Phidget
						device = new P_Voltage_Input(myParent, this, deviceType, serialNum, hubPort, chNum);
						break;
						
					default:
						System.out.println("device " + deviceType + " has no secondary I/O of type \"voltageInput\"");	
						break;
				}
				break;

			default:
				System.out.println("unknown secondary I/O: " + secondaryIO + ". currently, possible secondary I/Os are: digitalInput, analogInput.");	
				break;
			}
		}

		else {  // no secondary I/O stated
			switch (deviceType) {
			case "1046": // wheatstone bridge
			case "1101": // IR distance adapter (to Sharp proximity sensors)
			case "1102": // IR Reflective Sensor 5mm
			case "1103": // IR Reflective Sensor 10cm
			case "1104": // Vibration Sensor
			case "1105": // Light Sensor
			case "1106": // Force Sensor
			case "1107": // Humidity Sensor
			case "1108": // Magnetic Sensor
			case "1109": // Rotation Sensor
			case "1110": // Touch Sensor
			case "1111": // Motion Sensor (PIR)
			case "1112": // Slider 60
			case "1113": // Mini Joy Stick Sensor
			case "1115": // Pressure Sensor
			case "1116": // Multi-turn Rotation Sensor
			case "1118": // 50Amp Current Sensor AC / DC
			case "1119": // 20Amp Current Sensor AC / DC
			case "1120": // FlexiForce Adapter
			case "1121": // Voltage Divider
			case "1122": // 30 Amp Current Sensor AC / DC
			case "1124": // Precision Temperature Sensor
			case "1125": // Humidity/Temperature Sensor
			case "1126": // Differential Air Pressure Sensor +- 25kPa
			case "1128": // MaxBotix EZ-1 Sonar Sensor
			case "1129": // Touch Sensor
			case "1131": // Thin Force Sensor
			case "1134": // Switchable Voltage Divider
			case "1136": // Differential Air Pressure Sensor +-2 kPa
			case "1137": // Differential Air Pressure Sensor +-7 kPa
			case "1138": // Differential Air Pressure Sensor 50 kPa
			case "1139": // Differential Air Pressure Sensor 100 kPa
			case "1140": // Absolute Air Pressure Sensor 20-400 kPa
			case "1141": // Absolute Air Pressure Sensor 15-115 kPa
			case "1146": // IR Reflective Sensor 1-4mm
			case "3120": // Compression Load Cell (0-4.5 kg)
			case "3121": // Compression Load Cell (0-11.3 kg)
			case "3122": // Compression Load Cell (0-22.7 kg)
			case "3123": // Compression Load Cell (0-45.3 kg)
			case "3130": // Relative Humidity Sensor
			case "3520": // Sharp Distance Sensor (4-30cm)
			case "3521": // Sharp Distance Sensor (10-80cm)
			case "3522": // Sharp Distance Sensor (20-150cm)
			case "DAQ1500": // wheatstone bridge
			case "HIN1100": // Thumbstick Phidget
				device = new P_Voltage_Ratio(myParent, this, deviceType, serialNum, hubPort, chNum);
				break;

			case "SHARP2D120X": // Sharp Distance Sensor (4-30cm)
			case "2D120X": // Sharp Distance Sensor (4-30cm)
				deviceType = "1101";
				device = new P_Voltage_Ratio(myParent, this, deviceType, serialNum, hubPort, chNum);
				device.setSensorType("2D120X");
				break;
				
			case "SHARP2Y0A21": // Sharp Distance Sensor (10-80cm)
			case "2Y0A21": // Sharp Distance Sensor (10-80cm)
				deviceType = "1101";
				device = new P_Voltage_Ratio(myParent, this, deviceType, serialNum, hubPort, chNum);
				device.setSensorType("2Y0A21");
				break;

			case "SHARP2Y0A02": // Sharp Distance Sensor (20-150cm)
			case "2Y0A02": // Sharp Distance Sensor (20-150cm)
				deviceType = "1101";
				device = new P_Voltage_Ratio(myParent, this, deviceType, serialNum, hubPort, chNum);
				device.setSensorType("2Y0A02");
				break;

			case "1114": // Temperature Sensor
			case "1117": // Voltage Sensor
			case "1123": // Precision Voltage Sensor
			case "1127": // Precision Light Sensor
			case "1130": // pH/ORP Adapter
			case "1132": // 4-20mA Adapter
			case "1133": // Sound Sensor
			case "1135": // Precision Voltage Sensor
			case "1142": // Light Sensor 1000 lux
			case "1143": // Light Sensor 70000 lux
			case "3500": // AC Current Sensor 10Amp
			case "3501": // AC Current Sensor 25Amp
			case "3502": // AC Current Sensor 50Amp
			case "3503": // AC Current Sensor 100Amp
			case "3507": // AC Voltage Sensor 0-250V (50Hz)
			case "3508": // AC Voltage Sensor 0-250V (60Hz)
			case "3509": // DC Voltage Sensor 0-200V
			case "3510": // DC Voltage Sensor 0-75V
			case "3511": // DC Current Sensor 0-10mA
			case "3512": // DC Current Sensor 0-100mA
			case "3513": // DC Current Sensor 0-1A
			case "3514": // AC Active Power Sensor 0-250V*0-30A (50Hz)
			case "3515": // AC Active Power Sensor 0-250V*0-30A (60Hz)
			case "3516": // AC Active Power Sensor 0-250V*0-5A (50Hz)
			case "3517": // AC Active Power Sensor 0-250V*0-5A (60Hz)
			case "3518": // AC Active Power Sensor 0-110V*0-5A (60Hz)
			case "3519": // AC Active Power Sensor 0-110V*0-15A (60Hz)
			case "3584": // 0-50A DC Current Transducer
			case "3585": // 0-100A DC Current Transducer
			case "3586": // 0-250A DC Current Transducer
			case "3587": // +-50A DC Current Transducer
			case "3588": // +-100A DC Current Transducer
			case "3589": // +-250A DC Current Transducer
			case "MOT2002": // Motion Sensor (PIR)
				device = new P_Voltage_Input(myParent, this, deviceType, serialNum, hubPort, chNum);
				break;

			case "1015": // Linear Touch
				device = new P_Capacitive_Touch(myParent, this, deviceType, serialNum, hubPort, chNum);
				break;

			case "HIN1000": // Touch Keypad
				if (chNum == -1) chNum = 0;  // default touch sensor - 0
				device = new P_Capacitive_Touch(myParent, this, deviceType, serialNum, hubPort, chNum);
				break;

			case "HIN1001": // Touch Wheel
				if (chNum == -1) chNum = 4;  // default touch sensor - touch wheel (and not the single digits)
				device = new P_Capacitive_Touch(myParent, this, deviceType, serialNum, hubPort, chNum);
				break;

			case "SND1000": // Sound Sensor
				device = new P_Sound_Sensor(myParent, this, deviceType, serialNum, hubPort, chNum);
				break;

			case "1008":  // PhidgetAccelerometer 2-Axis
			case "1041":  // PhidgetSpatial 0/0/3 Basic
			case "1042":  // PhidgetSpatial 3/3/3 Basic
			case "1043":  // PhidgetSpatial Precision 0/0/3 High Resolution
			case "1044":  // PhidgetSpatial Precision 3/3/3 High Resolution
			case "1049":  // PhidgetSpatial 0/0/3
			case "1053":  // PhidgetAccelerometer 2-Axis
			case "1056":  // PhidgetSpatial 3/3/3
			case "1059":  // PhidgetAccelerometer 3-Axis
			case "MOT1100":  // Accelerometer Phidget
			case "MOT1101":  // Spatial Phidget
				device = new P_Spatial(myParent, this, deviceType, serialNum, hubPort, chNum);
				break;

			case "1000":   // PhidgetServo 1-Motor
			case "1001":   // PhidgetServo 4-Motor
			case "1061":   // PhidgetAdvancedServo 8-Motor
			case "1066":   // PhidgetAdvancedServo 1-Motor
			case "RCC0004":  // PhidgetAdvancedServo 8-Motor
			case "RCC1000":  // 16x RC Servo Phidget
				device = new P_RC_Servo(myParent, this, deviceType, serialNum, hubPort, chNum);
				break;

			default:
				System.out.println("Device type " + deviceType + " not supported yet");
				break;
			}	
		}
	}

	/**
	 * return the version of the Library.
	 * 
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}

	/**
	 * close the Phidget channel object
	 * 
	 */
	public void dispose() {
		device.close();
	}

	/**
	 * run events in the end of sketch's void draw(), in order to allow drawing inside event functions
	 * 
	 */
	public void draw() {
		device.draw();
	}
	
	
	/**
	 * print general help message to the console.
	 * 
	public static void help() {
		System.out.println(
				  "To add a Phidget device to your sketch, do the following steps:\n"
				+ "1. Connect the device using a VINT Hub, a PhidgetInterfaceKit or directly to USB. If the hub used has more than one channel, connect to lowest\n"
				+ "    channel number - usually 0.\n"
				+ "2. In the Phidgets Control Panel (installed from Phidgets.com), make sure your device works properly. See website for instructions.\n"
				+ "3. Add a Phidget Channel object to the sketch (usually before void setup()), such as: Channel myChannel\n"
				+ "4. Initialize the channel inside void setup(). There are several ways to do that, and it depends on your setup - what device you connect and \n"
				+ "    whether you connect other devices. In the most basic way, you only need to write the device type. This is a 4 digits, or 3 letters and 4 digits\n"
				+ "    printed on the device. e.g: \"1033\" or \"HIN1001\":\n"
				+ "     myChannel = Channel(this, \"HIN1001\");\n"
				+ "       myChannel - name of the Phidget channel you declared\n"
				+ "       this - don't change (it's needed for initialization)\n"
				+ "       \"HIN100\" - change to the name of your connected device\n"
				+ "5. Add \"myChannel.functions();\" to your void setup() to see specific instructions for using your device.    \n"
				+ "\n"
				+ "If you have more than one device connected or the above init() function doesn't work, you might need to add some information to help the system\n"
				+ " understand which device you meen. For example, let's assume you have 2 sensors connected to a VINT hub: ... TBC\n"
				+ "\n"
				+ "If you can't see the entire message above, please expand the console area (drag the seperating line upwards)."
				+ "\n"
				+ " For bug reports or questions, write in github or directly to Shachar at s.geiger.s@gmail.com");
	}

	/**
	 * print a specific help message for a specific device.
	 * 
	public static void help(String type) {
		Device.help(type);
	}

	/**
	 * create and initialize the Phidget channel object
	 * 
	 * @param theParent the parent PApplet
	 * @param channel channel object to initialize
	 * @param deviceType name of device type to initialize

/*	public static Channel init(PApplet theParent, String deviceType) {
		return channel;
	}*/

}

