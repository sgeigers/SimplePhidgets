package shenkar.SimplePhidgets;

import com.phidget22.DistanceSensorSonarReflections;
import com.phidget22.NMEAData;
import processing.core.*;

/* ToDos:
 * - Check real-time event of encoder. seems like double and even triple calls to it when in changeTrigger = 0. (Maybe use millis() for checking calling periods)
 * - Change order of functions in all examples to "setup" functions and "draw" functions
 * - APIs left to implement: IR, BLDCMotor, HumiditySensor, PHSensor, PowerGuard, PressureSensor, ResistenceInput, VoltageOutput
 * - Add PAppletParent.exit(); to most errors...
 * - Add specific example for 1045
 * - [PROBABLY OK] if dual usage of same event function name fails - for every event (which has dual usage) check type of channel before invoking
 * - [CHECKED. NOT POSSIBLE] Check if possible to open both DCMotor and MotorPositionController at the same time for one device.
 * - Check where specific function screening for specific devices can be done using getChannelSubClass
 * - spatial: check quaternion update with applicable device (1044_1, MOT0109, MOT1102)
 */

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
**/

public class Channel {

	public final static String VERSION = "1.0.10";

	// myParent is a reference to the parent sketch
	PApplet myParent;
	String deviceType;
	Device device;

	// P_Voltage_Ratio, P_Voltage_Input, P_Temperature_Sensor
	/**
	 * most basic use for sensors. returns an integer between 0 an 1000 coresponding to reading of the sensor.
	 * 
	 * @return int
	 */
	public int read() {return device.read(); } // returns sensor value, between 0 and 1000. for digital sensors: 0 or 1.

	// P_Voltage_Ratio
	public boolean getBridgeEnabled() {return device.getBridgeEnabled(); }
	public void setBridgeEnabled(boolean bridgeEnabled) {device.setBridgeEnabled(bridgeEnabled); }
	public int getBridgeGain() {return device.getBridgeGain(); } // 1, 2, 4, 8, 16, 32, 64, 128
	public void setBridgeGain(int gain) {device.setBridgeGain(gain); } // 1, 2, 4, 8, 16, 32, 64, 128

	// P_Voltage_Ratio, P_Voltage_Input, P_Sound_Sensor, P_Capacitive_Touch, P_Spatial, P_RC_Servo, P_Stepper, P_Temperature_Sensor, P_Encoder, P_Light_Sensor, P_Current_Input, P_DCMotor, P_MotorPositionController, P_Distance_Sensor
	public int getDataInterval() {return device.getDataInterval(); }
	public void setDataInterval(int dataInterval) {device.setDataInterval(dataInterval); }
	public int getMinDataInterval() {return device.getMinDataInterval(); }  // milliseconds
	public int getMaxDataInterval() {return device.getMaxDataInterval(); }  // milliseconds

	// P_Voltage_Ratio, P_Voltage_Input, P_Sound_Sensor, P_Capacitive_Touch
	public void setReadChangeTrigger(int readChangeTrigger) {device.setReadChangeTrigger(readChangeTrigger); }

	// P_Voltage_Ratio, P_Voltage_Input, P_Temperature_Sensor, P_Light_Sensor
	public float getSensorValue() {return device.getSensorValue(); }
	public boolean getSensorValueValidity() {return device.getSensorValueValidity(); }  // to avoid getting an error every time the reading is outside scale (like when connecting Sharp proximity sensors)
	public float getSensorValueChangeTrigger() {return device.getSensorValueChangeTrigger(); }
	public void setSensorValueChangeTrigger(float sensorValueChangeTrigger) {device.setSensorValueChangeTrigger(sensorValueChangeTrigger); }
	public String getSensorUnit() {return device.getSensorUnit(); }

	// P_Voltage_Ratio, P_Voltage_Input	
	public String getSensorType() {return device.getSensorType(); }
	public void setSensorType(String sensorType) {device.setSensorType(sensorType); }

	// P_Voltage_Ratio	
	public float getVoltageRatio() {return device.getVoltageRatio(); }
	public float getMinVoltageRatio() {return device.getMinVoltageRatio(); }
	public float getMaxVoltageRatio() {return device.getMaxVoltageRatio(); }
	public float getVoltageRatioChangeTrigger() {return device.getVoltageRatioChangeTrigger(); }
	public void setVoltageRatioChangeTrigger(float voltageRatioChangeTrigger) {device.setVoltageRatioChangeTrigger(voltageRatioChangeTrigger); }
	public float getMinVoltageRatioChangeTrigger() {return device.getMinVoltageRatioChangeTrigger(); }
	public float getMaxVoltageRatioChangeTrigger() {return device.getMaxVoltageRatioChangeTrigger(); }

	// P_Voltage_Input, P_Digital_Input, P_Frequency_Counter, P_Current_Input
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

	// P_Temperature_Sensor
	public float getTemperature() {return device.getTemperature(); }
	public float getMinTemperature() {return device.getMinTemperature(); }
	public float getMaxTemperature() {return device.getMaxTemperature(); }
	public String getRTDType() {return device.getRTDType(); }
	public void setRTDType(String sensorType) {device.setRTDType(sensorType); }
	public int getRTDWireSetup() {return device.getRTDWireSetup(); }
	public void setRTDWireSetup(int setup) {device.setRTDWireSetup(setup); }
	public float getTemperatureChangeTrigger() {return device.getTemperatureChangeTrigger(); }
	public void setTemperatureChangeTrigger(float changeTrigger) {device.setTemperatureChangeTrigger(changeTrigger); }
	public float getMinTemperatureChangeTrigger() {return device.getMinTemperatureChangeTrigger(); }
	public float getMaxTemperatureChangeTrigger() {return device.getMaxTemperatureChangeTrigger(); }
	public String getThermocoupleType() {return device.getThermocoupleType(); }
	public void setThermocoupleType(String tcType) {device.setThermocoupleType(tcType); }
	
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

	// P_Digital_Input, P_Frequency_Counter
	public String getInputMode() {return device.getInputMode(); }
	public void setInputMode(String im) {device.setInputMode(im); }

	// P_Digital_Input
	public boolean getState() {return device.getState(); }
	
	// P_Digital_Output
	public void on() {device.on(); }
	public void off() {device.off(); }
	public void analogWrite(int dutyCycle) {device.analogWrite(dutyCycle); }
	public void setDutyCycle(float dutyCycle) {device.setDutyCycle(dutyCycle); }
	public float getMinDutyCycle() {return device.getMinDutyCycle(); }
	public float getMaxDutyCycle() {return device.getMaxDutyCycle(); }
	public float getLEDCurrentLimit() {return device.getLEDCurrentLimit(); }
	public void setLEDCurrentLimit(float LEDCurrentLimit) {device.setLEDCurrentLimit(LEDCurrentLimit); }
	public float getMinLEDCurrentLimit() {return device.getMinLEDCurrentLimit(); }
	public float getMaxLEDCurrentLimit() {return device.getMaxLEDCurrentLimit(); }
	public String getLEDForwardVoltage() {return device.getLEDForwardVoltage(); }
	public void setLEDForwardVoltage(String LEDFV) {device.setLEDForwardVoltage(LEDFV); }
	public void setFrequency(float frequency) {device.setFrequency(frequency); }
	public float getMinFrequency() {return device.getMinFrequency(); }

	// P_Digital_Output, P_MotorPositionController
	public float getDutyCycle() {return device.getDutyCycle(); }
	
	// P_Digital_Output, P_Frequency_Counter
	public float getFrequency() {return device.getFrequency(); }
	public float getMaxFrequency() {return device.getMaxFrequency(); }

	// P_Digital_Output, P_RC_Servo, P_Stepper, P_DCMotor, P_MotorPositionController
	public void enableFailsafe(int failsafeTime) {device.enableFailsafe(failsafeTime); }	
	public int getMinFailsafeTime() {return device.getMinFailsafeTime(); }
	public int getMaxFailsafeTime() {return device.getMaxFailsafeTime(); }
	public void resetFailsafe() {device.resetFailsafe(); }	

	// P_RC_Servo, P_Stepper
	public boolean getIsMoving() {return device.getIsMoving(); }

	// P_RC_Servo, P_Stepper, P_MotorPositionController
	public void setTargetPosition(float tgt) {device.setTargetPosition(tgt); }
	public float getTargetPosition() {return device.getTargetPosition(); }
	public boolean getEngaged() {return device.getEngaged(); }
	public void setEngaged(boolean eng) {device.setEngaged(eng); }
	public float getPosition() {return device.getPosition(); }
	public float getMinPosition() {return device.getMinPosition(); }
	public float getMaxPosition() {return device.getMaxPosition(); }
	public float getVelocityLimit() {return device.getVelocityLimit(); }
	public void setVelocityLimit(float vel) {device.setVelocityLimit(vel); }
	public float getMinVelocityLimit() {return device.getMinVelocityLimit(); }
	public float getMaxVelocityLimit() {return device.getMaxVelocityLimit(); }

	// P_RC_Servo, P_Stepper, P_DCMotor, P_MotorPositionController
	public float getAcceleration() {return device.getAcceleration(); }
	public void setAcceleration(float accel) {device.setAcceleration(accel); }
	public float getMinAcceleration() {return device.getMinAcceleration(); }
	public float getMaxAcceleration() {return device.getMaxAcceleration(); }

	// P_RC_Servo, P_Stepper, P_GPS, P_DCMotor
	public float getVelocity() {return device.getVelocity(); }

	// P_RC_Servo
	public void setAngle(float ang) {device.setAngle(ang); }
	public void setMinPosition(float pos) {device.setMinPosition(pos); }
	public void setMaxPosition(float pos) {device.setMaxPosition(pos); }
	public void setMinPulseWidth(float pls) {device.setMinPulseWidth(pls); }
	public float getMinPulseWidth() {return device.getMinPulseWidth(); }
	public void setMaxPulseWidth(float pls) {device.setMaxPulseWidth(pls); }
	public float getMaxPulseWidth() {return device.getMaxPulseWidth(); }
	public float getMinPulseWidthLimit() {return device.getMinPulseWidthLimit(); }
	public float getMaxPulseWidthLimit() {return device.getMaxPulseWidthLimit(); }
	public boolean getSpeedRampingState() {return device.getSpeedRampingState(); }
	public void setSpeedRampingState(boolean state) {device.setSpeedRampingState(state); }
	public float getTorque() {return device.getTorque(); }
	public void setTorque(float trq) {device.setTorque(trq); }
	public float getMinTorque() {return device.getMinTorque(); }
	public float getMaxTorque() {return device.getMaxTorque(); }
	public String getVoltageString() {return device.getVoltageString(); }
	public void setVoltage(String vol) {device.setVoltage(vol); }
	
	// P_Stepper
	public String getControlMode() {return device.getControlMode(); }
	public void setControlMode(String mode) {device.setControlMode(mode); }
	public float getHoldingCurrentLimit() {return device.getHoldingCurrentLimit(); }
	public void setHoldingCurrentLimit(float curr) {device.setHoldingCurrentLimit(curr); }

	// P_Stepper, P_MotorPositionController
	public void addPositionOffset(int offset) {device.addPositionOffset(offset); }
	public float getRescaleFactor() {return device.getRescaleFactor(); }
	public void setRescaleFactor(float fctr) {device.setRescaleFactor(fctr); }

	// P_Stepper, P_DCMotor, P_MotorPositionController
	public float getCurrentLimit() {return device.getCurrentLimit(); }
	public void setCurrentLimit(float curr) {device.setCurrentLimit(curr); }
	public float getMinCurrentLimit() {return device.getMinCurrentLimit(); }
	public float getMaxCurrentLimit() {return device.getMaxCurrentLimit(); }

	// P_RFID
	public boolean getTagPresent() {return device.getTagPresent(); }
	public String getLastTagString() {return device.getLastTagString(); }
	public String getLastTagProtocol() {return device.getLastTagProtocol(); }
	public void write(String tagString, String prot, boolean lock) {device.write(tagString, prot, lock); }
	public boolean getAntennaEnabled() {return device.getAntennaEnabled(); }
	public void setAntennaEnabled(boolean ant) {device.setAntennaEnabled(ant); }

	// P_Encoder
	public boolean getEnabled() {return device.getEnabled(); }
	public void setEnabled(boolean en) {device.setEnabled(en); }
	public long getIndexPosition() {return device.getIndexPosition(); }
	public long getEncPosition() {return device.getEncPosition(); }
	public void setEncPosition(long pos	) {device.setEncPosition(pos); }
	public int getPositionChangeTrigger() {return device.getPositionChangeTrigger(); }
	public void setPositionChangeTrigger(int trigger) {device.setPositionChangeTrigger(trigger); }
	public int getMinPositionChangeTrigger() {return device.getMinPositionChangeTrigger(); }
	public int getMaxPositionChangeTrigger() {return device.getMaxPositionChangeTrigger(); }

	// P_Encoder, P_MotorPositionController
	public String getIOMode() {return device.getIOMode(); }
	public void setIOMode(String em) {device.setIOMode(em); }

	// P_Light_Sensor
	public float getIlluminance() {return device.getIlluminance(); }	
	public float getMinIlluminance() {return device.getMinIlluminance(); }
	public float getMaxIlluminance() {return device.getMaxIlluminance(); }	
	public float getIlluminanceChangeTrigger() {return device.getIlluminanceChangeTrigger(); }	
	public void setIlluminanceChangeTrigger(float sensorValueChangeTrigger) {device.setIlluminanceChangeTrigger(sensorValueChangeTrigger); }	
	public float getMinIlluminanceChangeTrigger() {return device.getMinIlluminanceChangeTrigger(); }	
	public float getMaxIlluminanceChangeTrigger() {return device.getMaxIlluminanceChangeTrigger(); }

	// P_Frequency_Counter
	public long getCount() {return device.getCount(); }
	public String getFilterType() {return device.getFilterType(); }
	public void setFilterType(String ft) {device.setFilterType(ft); }
	public float getFrequencyCutoff() {return device.getFrequencyCutoff();}
	public void setFrequencyCutoff(float cutoff) {device.setFrequencyCutoff(cutoff); }
	public float getMinFrequencyCutoff() {return device.getMinFrequencyCutoff(); }
	public float getMaxFrequencyCutoff() {return device.getMaxFrequencyCutoff(); }
	public void reset() {device.reset(); }
	public float getTimeElapsed() {return device.getTimeElapsed(); }
	
	// P_Current_Input
	public float getCurrent() {return device.getCurrent(); }
	public float getMinCurrent() {return device.getMinCurrent(); }
	public float getMaxCurrent() {return device.getMaxCurrent(); }
	public boolean getCurrentValidity() {return device.getCurrentValidity(); }
	public float getCurrentChangeTrigger() {return device.getCurrentChangeTrigger(); }
	public void setCurrentChangeTrigger(float changeTrigger) {device.setCurrentChangeTrigger(changeTrigger); }
	public float getMinCurrentChangeTrigger() {return device.getMinCurrentChangeTrigger(); }
	public float getMaxCurrentChangeTrigger() {return device.getMaxCurrentChangeTrigger(); }
	
	// P_GPS
	public float getAltitude() {return device.getAltitude(); }
	public int getDay() {return device.getDay(); }
	public int getMonth() {return device.getMonth(); }
	public int getYear() {return device.getYear(); }
	public java.util.Calendar getDateAndTime() {return device.getDateAndTime(); }
	public float getHeading() {return device.getHeading(); }
	public float getLatitude() {return device.getLatitude(); }
	public float getLongitude() {return device.getLongitude(); }
	public boolean getPositionFixState() {return device.getPositionFixState(); }
	public int getMilliseconds() {return device.getMilliseconds(); }
	public int getSeconds() {return device.getSeconds(); }
	public int getMinutes() {return device.getMinutes(); }
	public int getHours() {return device.getHours(); }
	public NMEAData getNMEAData() {return device.getNMEAData(); }
	
	// P_DCMotor
	public void setTargetVelocity(float vel) {device.setTargetVelocity(vel); }
	public float getTargetVelocity() {return device.getTargetVelocity(); }
	public float getBackEMF() {return device.getBackEMF(); }
	public boolean getBackEMFSensingState() {return device.getBackEMFSensingState(); }
	public void setBackEMFSensingState(boolean state) {device.setBackEMFSensingState(state); }
	public float getBrakingStrength() {return device.getBrakingStrength(); }
	public float getMinBrakingStrength() {return device.getMinBrakingStrength(); }
	public float getMaxBrakingStrength() {return device.getMaxBrakingStrength(); }
	public float getTargetBrakingStrength() {return device.getTargetBrakingStrength(); }
	public void setTargetBrakingStrength(float strength) {device.setTargetBrakingStrength(strength); }
	public float getMinVelocity() {return device.getMinVelocity(); }
	public float getMaxVelocity() {return device.getMaxVelocity(); }

	// P_DCMotor, P_MotorPositionController
	public float getCurrentRegulatorGain() {return device.getCurrentRegulatorGain(); }
	public void setCurrentRegulatorGain(float gain) {device.setCurrentRegulatorGain(gain); }
	public float getMinCurrentRegulatorGain() {return device.getMinCurrentRegulatorGain(); }
	public float getMaxCurrentRegulatorGain() {return device.getMaxCurrentRegulatorGain(); }
	public String getFanMode() {return device.getFanMode(); }
	public void setFanMode(String mode) {device.setFanMode(mode); }

	// P_MotorPositionController
	public void setDeadBand(float accel) {device.setDeadBand(accel); }
	public float getDeadBand() {return device.getDeadBand(); }
	public float getKd() {return device.getKd(); }
	public void setKd(float kd) {device.setKd(kd); }
	public float getKi() {return device.getKi(); }
	public void setKi(float ki) {device.setKi(ki); }
	public float getKp() {return device.getKp(); }
	public void setKp(float kp) {device.setKp(kp); }
	public void addPositionOffset(float offset) {device.addPositionOffset(offset); }
	public float getStallVelocity() {return device.getStallVelocity(); }
	public void setStallVelocity(float vel) {device.setStallVelocity(vel); }
	public float getMinStallVelocity() {return device.getMinStallVelocity(); }
	public float getMaxStallVelocity() {return device.getMaxStallVelocity(); }

	// P_Distance_Sensor
	public int getDistance() {return device.getDistance(); }
	public int getMinDistance() {return device.getMinDistance(); }
	public int getMaxDistance() {return device.getMaxDistance(); }
	public int getDistanceChangeTrigger() {return device.getDistanceChangeTrigger(); }
	public void setDistanceChangeTrigger(int trigger) {device.setDistanceChangeTrigger(trigger); }
	public int getMinDistanceChangeTrigger() {return device.getMinDistanceChangeTrigger(); }
	public int getMaxDistanceChangeTrigger() {return device.getMaxDistanceChangeTrigger(); }
	public void setSonarQuietMode(boolean mode) {device.setSonarQuietMode(mode); }
	public boolean getSonarQuietMode() {return device.getSonarQuietMode(); }
	public DistanceSensorSonarReflections getSonarReflections() {return device.getSonarReflections(); }

	// P_LCD
	public float getBacklight() {return device.getBacklight(); }	
	public void setBacklight(float light) {device.setBacklight(light); }	
	public float getMinBacklight() {return device.getMinBacklight(); }	
	public float getMaxBacklight() {return device.getMaxBacklight(); }	
	public void setCharacterBitmap(String fontName, String character, byte[] bitmap) {device.setCharacterBitmap(fontName, character, bitmap); }	
	public void setCharacterBitmap(String character, byte[] bitmap) {device.setCharacterBitmap(character, bitmap); }	
	public int getMaxCharacters(String fontName) {return device.getMaxCharacters(fontName); }	
	public int getMaxCharacters() {return device.getMaxCharacters(); }	
	public void clear() {device.clear(); }	
	public float getContrast() {return device.getContrast(); }	
	public void setContrast(float contrast) {device.setContrast(contrast); }	
	public float getMinContrast() {return device.getMinContrast(); }	
	public float getMaxContrast() {return device.getMaxContrast(); }	
	public void copy(int sourceFramebuffer,	int destFramebuffer, int sourceX1, int sourceY1, int sourceX2, int sourceY2, int destX, int destY, boolean inverted) {device.copy(sourceFramebuffer, destFramebuffer, sourceX1, sourceY1, sourceX2, sourceY2, destX, destY, inverted); }
	public boolean getCursorBlink() {return device.getCursorBlink(); }	
	public void setCursorBlink(boolean blink) {device.setCursorBlink(blink); }	
	public boolean getCursorOn() {return device.getCursorOn(); }	
	public void setCursorOn(boolean on) {device.setCursorOn(on); }	
	public void drawLine(int x1, int y1, int x2, int y2) {device.drawLine(x1, y1, x2, y2); }	
	public void drawPixel(int x, int y, String pixelState) {device.drawPixel(x, y, pixelState); }	
	public void drawRect(int x1, int y1, int x2, int y2, boolean filled, boolean inverted) {device.drawRect(x1, y1, x2, y2, filled, inverted); }	
	public void flush() {device.flush(); }	
	public void setFontSize(String fontName, int width, int height) {device.setFontSize(fontName, width, height); }	
	public int getFrameBuffer() {return device.getFrameBuffer(); }	
	public void setFrameBuffer(int buffer) {device.setFrameBuffer(buffer); }	
	public int getHeight() {return device.getHeight(); }	
	public int getWidth() {return device.getWidth(); }	
	public void initialize() {device.initialize(); }	
	public void saveFrameBuffer(int buffer) {device.saveFrameBuffer(buffer); }
	public String getScreenSize() {return device.getScreenSize(); }	
	public void setScreenSize(String size) {device.setScreenSize(size); }	
	public boolean getSleeping() {return device.getSleeping(); }	
	public void setSleeping(boolean on) {device.setSleeping(on); }	
	public void writeBitmap(int xPosition, int yPosition, int xSize, int ySize, byte[] bitmap) {device.writeBitmap(xPosition, yPosition, xSize, ySize, bitmap); }
	public void writeText(String fontName, int xPosition, int yPosition, String text) {device.writeText(fontName, xPosition, yPosition, text); }	
	public void writeText(int xPosition, int yPosition, String text) {device.writeText(xPosition, yPosition, text); }		
	
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
	 * broaderish constructor
	 * 
	 * @param theParent the parent PApplet
	 * @param type 4 numbers or 3 letters and 4 numbers describing type of device to associate to this channel
	 * @param serialNum serial number of hub or interfaceKit (to be used in case there are more than one hub/IK connected to the computer)
	 * @param hubPort the port number of hub or interfaceKit where the device is connected, or hub/IK serial number (if number used has more than 4 digits)
	 * @param chNum specify the channel used (see general constructor), or hub port if serial number was specified in previous parameter
	 */
	public Channel(PApplet theParent, String type, int serialNum, int hubPort, int chNum) {
		this(theParent, type, serialNum, hubPort, chNum, "");
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
	 * @param serialNum serial number of hub or interfaceKit (to be used in case there are more than one hub/IK connected to the computer)
	 * @param hubPort the port number of hub or interfaceKit where the device is connected, or hub/IK serial number (if number used has more than 4 digits)
	 * @param secondaryIO either "digital_input" or "digital_output" to indicate digital channel (unless trivial by board type)
	 * @param chNum specify the channel used (see general constructor), or hub port if serial number was specified in previous parameter
	 */
	public Channel(PApplet theParent, String type, int serialNum, int hubPort, String secondaryIO, int chNum) {
		this(theParent, type, serialNum, hubPort, chNum, secondaryIO);
	}

	/**
	 *  the most general constructor w/ secondary I/O 2
	 * 
	 * @param theParent the parent PApplet
	 * @param type 4 numbers or 3 letters and 4 numbers describing type of device to associate to this channel
	 * @param serialNum serial number of hub or interfaceKit (to be used in case there are more than one hub/IK connected to the computer)
	 * @param secondaryIO either "digital_input" or "digital_output" to indicate digital channel (unless trivial by board type)
	 * @param hubPort the port number of hub or interfaceKit where the device is connected, or hub/IK serial number (if number used has more than 4 digits)
	 * @param chNum specify the channel used (see general constructor), or hub port if serial number was specified in previous parameter
	 */
	public Channel(PApplet theParent, String type, int serialNum, String secondaryIO, int hubPort, int chNum) {
		this(theParent, type, serialNum, hubPort, chNum, secondaryIO);
	}

	/**
	 *  the most general constructor w/ secondary I/O 3
	 * 
	 * @param theParent the parent PApplet
	 * @param type 4 numbers or 3 letters and 4 numbers describing type of device to associate to this channel
	 * @param secondaryIO either "digital_input" or "digital_output" to indicate digital channel (unless trivial by board type)
	 * @param serialNum serial number of hub or interfaceKit (to be used in case there are more than one hub/IK connected to the computer)
	 * @param hubPort the port number of hub or interfaceKit where the device is connected, or hub/IK serial number (if number used has more than 4 digits)
	 * @param chNum specify the channel used (see general constructor), or hub port if serial number was specified in previous parameter
	 */
	public Channel(PApplet theParent, String type, String secondaryIO, int serialNum, int hubPort, int chNum) {
		this(theParent, type, serialNum, hubPort, chNum, secondaryIO);
	}



	/**
	 * the most general constructor w/ secondary I/O 4
	 * 
	 * @param theParent the parent PApplet
	 * @param type 4 numbers or 3 letters and 4 numbers describing type of device to associate to this channel
	 * @param serialNum serial number of hub or interfaceKit (to be used in case there are more than one hub/IK connected to the computer)
	 * @param hubPort the port number of hub or interfaceKit where the device is connected. Only needed if different than 0 or if a channel number is specified
	 * @param chNum if the device has more than one channel (e.g. wheatstone bridge) - specify the channel used
	 */
	public Channel(PApplet theParent, String type, int serialNum, int hubPort, int chNum, String secondaryIO) {
		myParent = theParent;
		myParent.registerMethod("pre", this);
		myParent.registerMethod("draw", this);
		myParent.registerMethod("dispose", this);
		if (hubPort > 9999) {
			int h = serialNum;
			serialNum = hubPort;
			hubPort = h;
			if (hubPort == -1) {
				hubPort = chNum;
				chNum = h;
			}
			if (hubPort == -1) {
				hubPort = 0;
			}
		}
		else if (chNum > 9999) {
			int c = serialNum;
			serialNum = chNum;
			chNum = hubPort;
			hubPort = c;
		}

		System.out.println("Type: "+type + " \tSerial: "+serialNum +"\thubPort: "+hubPort+ "\tchannel: "+chNum+ "\tsecIO: "+secondaryIO);
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

			case "DIGITALOUTPUT":
				switch (deviceType) {
					case "1010": // PhidgetInterfaceKit 8/8/8
					case "1011": // PhidgetInterfaceKit 2/2/2
					case "1012": // PhidgetInterfaceKit 0/16/16
					case "1013": // PhidgetInterfaceKit 8/8/8
					case "1014": // PhidgetInterfaceKit 0/0/4 (Relays)
					case "1017": // PhidgetInterfaceKit 0/0/8 (Relays)
					case "1018": // PhidgetInterfaceKit 8/8/8
					case "1019": // PhidgetInterfaceKit 8/8/8
					case "1023": // PhidgetRFID
					case "1024": // PhidgetRFID Read-Write
					case "1030": // PhidgetLED-64
					case "1031": // PhidgetLED-64 Advanced
					case "1032": // PhidgetLED-64 Advanced
					case "1070": // PhidgetSBC
					case "1072": // PhidgetSBC2
					case "1073": // PhidgetSBC3
					case "1202": // PhidgetTextLCD 20X2 : Blue : Integrated PhidgetInterfaceKit 8/8/8
					case "1203": // PhidgetTextLCD 20X2 : White : Integrated PhidgetInterfaceKit 8/8/8
					case "1219": // PhidgetTextLCD 20X2 White with PhidgetInterfaceKit 0/8/8
					case "1220": // PhidgetTextLCD 20X2 Blue with PhidgetInterfaceKit 0/8/8
					case "1221": // PhidgetTextLCD 20X2 Green with PhidgetInterfaceKit 0/8/8
					case "1222": // PhidgetTextLCD 20X2 Red with PhidgetInterfaceKit 0/8/8
					case "LED1000": // 32x Isolated LED Phidget
					case "HUB0000": // 6-Port USB VINT Hub Phidget
					case "HUB5000": // 6-Port Network VINT Hub Phidget
					case "OUT1100": // 4x Digital Output Phidget
					case "REL1000": // 4x Relay Phidget
					case "REL1100": // 4x Isolated Solid State Relay Phidget
					case "REL1101": // 16x Isolated Solid State Relay Phidget
					case "SBC3003": // PhidgetSBC4 - 6-Port VINT Hub Phidget
						device = new P_Digital_Output(myParent, this, deviceType, serialNum, hubPort, chNum);
						break;
	
					default:
						System.out.println("device " + deviceType + " has no secondary I/O of type \"digitalOutput\"");	
						break;
				}
				break;
			
			case "ANALOGINPUT":
			case "VOLTAGERATIO":
				switch (deviceType) {
					case "1010": // PhidgetInterfaceKit 8/8/8
					case "1011": // PhidgetInterfaceKit 2/2/2
					case "1013": // PhidgetInterfaceKit 8/8/8
					case "1018": // PhidgetInterfaceKit 8/8/8
					case "1019": // PhidgetInterfaceKit 8/8/8
					case "1070": // PhidgetSBC
					case "1072": // PhidgetSBC2
					case "1073": // PhidgetSBC3
					case "1202": // PhidgetTextLCD 20X2 : Blue : Integrated PhidgetInterfaceKit 8/8/8
					case "1203": // PhidgetTextLCD 20X2 : White : Integrated PhidgetInterfaceKit 8/8/8
					case "DAQ1000": // 8x Voltage Input Phidget
					case "DCC1000": // DC Motor Phidget
					case "HIN1100": // Thumbstick Phidget
					case "HUB0000": // 6-Port USB VINT Hub Phidget
					case "HUB5000": // 6-Port Network VINT Hub Phidget
					case "SBC3003": // PhidgetSBC4 - 6-Port VINT Hub Phidget
						device = new P_Voltage_Ratio(myParent, this, deviceType, serialNum, hubPort, chNum);
						break;
						
					default:
						System.out.println("device " + deviceType + " has no secondary I/O of type \"analogInput\"");	
						break;
				}
				break;

			case "VOLTAGE":
			case "VOLTAGEINPUT":
				switch (deviceType) {
					case "1010": // PhidgetInterfaceKit 8/8/8
					case "1011": // PhidgetInterfaceKit 2/2/2
					case "1013": // PhidgetInterfaceKit 8/8/8
					case "1018": // PhidgetInterfaceKit 8/8/8
					case "1019": // PhidgetInterfaceKit 8/8/8
					case "1051": // PhidgetTemperatureSensor 1-Input
					case "1058": // PhidgetPhSensor
					case "1065":  // PhidgetMotorControl 1-Motor - has 2 voltage channels: external and supply voltage
					case "1070": // PhidgetSBC
					case "1072": // PhidgetSBC2
					case "1073": // PhidgetSBC3
					case "1202": // PhidgetTextLCD 20X2 : Blue : Integrated PhidgetInterfaceKit 8/8/8
					case "1203": // PhidgetTextLCD 20X2 : White : Integrated PhidgetInterfaceKit 8/8/8
					case "ADP1000": // pH Phidget
					case "DAQ1000": // 8x Voltage Input Phidget
					case "HUB0000": // 6-Port USB VINT Hub Phidget
					case "HUB5000": // 6-Port Network VINT Hub Phidget
					case "PRX2300": // Beam Break Phidget - default is digital input
					case "SAF1000": // Programmable Power Guard Phidget
					case "TMP1100": // Isolated Thermocouple Phidget
					case "TMP1101": // 4x Thermocouple Phidget
						device = new P_Voltage_Input(myParent, this, deviceType, serialNum, hubPort, chNum);
						break;
						
					default:
						System.out.println("device " + deviceType + " has no secondary I/O of type \"voltageInput\"");	
						break;
				}
				break;

			case "TEMP":
			case "TEMPERATURE":
			case "TEMPERATURESENSOR":
				switch (deviceType) {
					case "DCC1000": // DC Motor Phidget
					case "DCC1100": // Brushless DC Motor Phidget
					case "HUM1000": // Humidity Phidget
					case "HUM1001": // Humidity Phidget
					case "MOT0109":	 // PhidgetSpatial Precision 3/3/3
					case "SAF1000": // Programmable Power Guard Phidget
						device = new P_Temperature_Sensor(myParent, this, deviceType, serialNum, hubPort, chNum);
						break;
						
					default:
						System.out.println("device " + deviceType + " has no secondary I/O of type \"voltageInput\"");	
						break;
				}
				break;

			case "ENC":
			case "ENCODER":
				switch (deviceType) {
					case "1065": // PhidgetMotorControl 1-Motor
					case "DCC1000": // DC Motor Phidget
					case "DCC1001": // 2A DC Motor Phidget
					case "DCC1002": // 4A DC Motor Phidget
						device = new P_Encoder(myParent, this, deviceType, serialNum, hubPort, chNum);
						break;

					default:
						System.out.println("device " + deviceType + " has no secondary I/O of type \"Encoder\"");	
						break;
				}
				break;
				
			case "FREQ":
			case "FREQUENCY":
			case "FREQUENCYCOUNTER":
			case "FREQUENCY_COUNTER":
			case "COUNTER":
				switch (deviceType) {
					case "DAQ1400": // Versatile Input Phidget
						device = new P_Frequency_Counter(myParent, this, deviceType, serialNum, hubPort, chNum);
						break;

					default:
						System.out.println("device " + deviceType + " has no secondary I/O of type \"FrequencyCounter\"");	
						break;
				}
				break;
				
			case "CURRENT":
			case "CURRENTINPUT":
			case "CURRENT_INPUT":
			case "CURRENTSENSOR":
			case "CURRENT_SENSOR":
				switch (deviceType) {
					case "1061":  // PhidgetAdvancedServo 8-Motor
					case "1063":  // PhidgetStepper Bipolar 1-Motor
					case "1064":  // PhidgetMotorControl HC
					case "1065":  // PhidgetMotorControl 1-Motor
					case "1066":   // PhidgetAdvancedServo 1-Motor
					case "DAQ1400": // Versatile Input Phidget
					case "DCC1000": // DC Motor Phidget				
					case "VCP1100": // 30A Current Sensor Phidget
						device = new P_Current_Input(myParent, this, deviceType, serialNum, hubPort, chNum);
						break;
	
					default:
						System.out.println("device " + deviceType + " has no secondary I/O of type \"currentInput\"");	
						break;
				}
				break;

			case "MOTOR_POSITION_CONTROLLER":
			case "MOTORPOSITIONCONTROLLER":
			case "POSITIONCONTROLLER":
			case "POSITIONCONTROL":
				switch (deviceType) {
					case "DCC1000":  // DC Motor Phidget
					case "DCC1001":  // 2A DC Motor Phidget
					case "DCC1002":  // 4A DC Motor Phidget
					case "DCC1100":  // Brushless DC Motor Phidget
						device = new P_MotorPositionController(myParent, this, deviceType, serialNum, hubPort, chNum);
						break;
	
					default:
						System.out.println("device " + deviceType + " has no secondary I/O of type \"motorPositionController\"");	
						break;
				}
				break;

			case "LCD":
				switch (deviceType ) {
					case "1202": // PhidgetTextLCD 20X2 : Blue : Integrated PhidgetInterfaceKit 8/8/8
					case "1203": // PhidgetTextLCD 20X2 : White : Integrated PhidgetInterfaceKit 8/8/8
					case "1219": // PhidgetTextLCD 20X2 White with PhidgetInterfaceKit 0/8/8
					case "1220": // PhidgetTextLCD 20X2 Blue with PhidgetInterfaceKit 0/8/8
					case "1221": // PhidgetTextLCD 20X2 Green with PhidgetInterfaceKit 0/8/8
					case "1222": // PhidgetTextLCD 20X2 Red with PhidgetInterfaceKit 0/8/8
						device = new P_LCD(myParent, this, deviceType, serialNum, hubPort, chNum);
						break;
	
					default:
						System.out.println("device " + deviceType + " has no secondary I/O of type \"LCD\"");	
						break;
				}
				break;
				
			default:
				System.out.println("unknown secondary I/O: " + secondaryIO + ". currently, possible secondary I/Os are: digitalInput, digitalOutput, analogInput, voltageInput, temperatureSensor, encoder, frequencyCounter, currentInput, positionControl and LCD.");	
				myParent.exit();
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
			case "3583": // Rotary Potentiometer - WDA-D35-D4C
			case "DAQ1000": // 8x Voltage Input Phidget
			case "DAQ1500": // wheatstone bridge
			case "HIN1100": // Thumbstick Phidget
				device = new P_Voltage_Ratio(myParent, this, deviceType, serialNum, hubPort, chNum);
				break;

			case "3132": // Micro Load Cell (0-780g) - CZL616C
			case "3133": // Micro Load Cell (0-5kg) - CZL635
			case "3134": // Micro Load Cell (0-20kg) - CZL635
			case "3135": // Micro Load Cell (0-50kg) - CZL635
			case "3136": // Button Load Cell (0-50kg) - CZL204E
			case "3137": // Button Load Cell (0-200kg) - CZL204E
			case "3138": // S Type Load Cell (0-100kg) - CZL301C
			case "3139": // Micro Load Cell (0-100g) - CZL639HD
			case "3140": // S Type Load Cell (0-500kg) - CZL301
			case "3141": // Button Load Cell (0-1000kg) - CZL204
			case "FRC4114": // Micro Load Cell (0-780g) - CZL611CD
			case "FRC4115": // Micro Load Cell (0-5kg) - CZL611CD
			case "FRC4116": // Micro Load Cell (0-25kg) - CZL611CD
				System.out.println("For load cells, use the device type of the board it's connected to (\"DAQ1500\" or \"1046\" instead of \"" + deviceType + "\")");
				myParent.exit();
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
			case "VCP4114": // Clip-on Current Transducer 25A
				device = new P_Voltage_Input(myParent, this, deviceType, serialNum, hubPort, chNum);
				break;

			case "1014": // PhidgetInterfaceKit 0/0/4 (Relays)
			case "1017": // PhidgetInterfaceKit 0/0/8 (Relays)
			case "1030": // PhidgetLED-64
			case "1031": // PhidgetLED-64 Advanced
			case "1032": // PhidgetLED-64 Advanced
			case "LED1000": // 32x Isolated LED Phidget
			case "OUT1100": // 4x Digital Output Phidget
			case "PSU1000": // Power Plug Phidget
			case "PSU2000": // DC Power Source 5V
			case "PSU2001": // DC Power Source 1.5 - 5V
			case "PSU2002": // DC Power Source 5 - 24V
			case "REL1000": // 4x Relay Phidget
			case "REL1100": // 4x Isolated Solid State Relay Phidget
			case "REL1101": // 16x Isolated Solid State Relay Phidget
				device = new P_Digital_Output(myParent, this, deviceType, serialNum, hubPort, chNum);
				break;

			case "1015": // Linear Touch
			case "1016": // Circular Touch
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
			case "MOT0109":	 // PhidgetSpatial Precision 3/3/3
			case "MOT1100":  // Accelerometer Phidget
			case "MOT1101":  // Spatial Phidget
			case "MOT1102":  // Spatial Phidget
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

			case "1062":   // PhidgetStepper Unipolar 4-Motor
			case "1063":   // PhidgetStepper Bipolar 1-Motor
			case "1067":   // PhidgetStepper Bipolar HC
			case "STC1000":  // Stepper Phidget
			case "STC1001":  // 2.5A Stepper Phidget
			case "STC1002":  // 8A Stepper Phidget
			case "STC1003":  // 4A Stepper Phidget
				device = new P_Stepper(myParent, this, deviceType, serialNum, hubPort, chNum);
				break;

			case "1045": // PhidgetTemperatureSensor IR
			case "1048": // PhidgetTemperatureSensor 4-Input
			case "1051": // PhidgetTemperatureSensor 1-Input
			case "TMP1000": // Temperature Phidget
			case "TMP1100": // Isolated Thermocouple Phidget
			case "TMP1101": // 4x Thermocouple Phidget
			case "TMP1200": // RTD Phidget
				device = new P_Temperature_Sensor(myParent, this, deviceType, serialNum, hubPort, chNum);
				break;
				
			case "1024": // PhidgetRFID Read-Write
				device = new P_RFID(myParent, this, deviceType, serialNum, hubPort, chNum);
				break;
				
			case "1047": // PhidgetEncoder HighSpeed 4-Input
			case "1052": // PhidgetEncoder
			case "1057": // PhidgetEncoder HighSpeed
			case "ENC1000": // Quadrature Encoder Phidget
			case "HIN1101": // Dial Phidget
				device = new P_Encoder(myParent, this, deviceType, serialNum, hubPort, chNum);
				break;

			case "LUX1000": // Light Phidget
				device = new P_Light_Sensor(myParent, this, deviceType, serialNum, hubPort, chNum);
				break;

			case "1054": // Phidget FrequencyCounter
				device = new P_Frequency_Counter(myParent, this, deviceType, serialNum, hubPort, chNum);
				break;

			case "VCP1100": // 30A Current Sensor Phidget
				device = new P_Current_Input(myParent, this, deviceType, serialNum, hubPort, chNum);
				break;

			case "1040": // PhidgetGPS
				device = new P_GPS(myParent, this, deviceType, serialNum, hubPort, chNum);
				break;

			case "1060":  // PhidgetMotorControl LV
			case "1064":  // PhidgetMotorControl HC
			case "1065":  // PhidgetMotorControl 1-Motor
			case "DCC1000":  // DC Motor Phidget
			case "DCC1001":  // 2A DC Motor Phidget
			case "DCC1002":  // 4A DC Motor Phidget
			case "DCC1003":  // 2x DC Motor Phidget
				device = new P_DCMotor(myParent, this, deviceType, serialNum, hubPort, chNum);
				break;

			case "PRX2300":  // Beam Break Phidget - also possible to use voltageInput as secondary
				// this might be problematic for old InterfaceKits, but left as is for lack of interest...
				device = new P_Digital_Input(myParent, this, deviceType, serialNum, hubPort, chNum);
				break;

			case "DST1000":  // Distance Phidget (170mm)
			case "DST1001":  // Distance Phidget (650mm)
			case "DST1002":  // Distance Phidget 1300mm
			case "DST1200":  // Sonar Phidget
				device = new P_Distance_Sensor(myParent, this, deviceType, serialNum, hubPort, chNum);
				break;

			case "1202": // PhidgetTextLCD 20X2 : Blue : Integrated PhidgetInterfaceKit 8/8/8
			case "1203": // PhidgetTextLCD 20X2 : White : Integrated PhidgetInterfaceKit 8/8/8
			case "1204": // PhidgetTextLCD Adapter
			case "1219": // PhidgetTextLCD 20X2 White with PhidgetInterfaceKit 0/8/8
			case "1220": // PhidgetTextLCD 20X2 Blue with PhidgetInterfaceKit 0/8/8
			case "1221": // PhidgetTextLCD 20X2 Green with PhidgetInterfaceKit 0/8/8
			case "1222": // PhidgetTextLCD 20X2 Red with PhidgetInterfaceKit 0/8/8
			case "LCD1100":  // Graphic LCD Phidget
				device = new P_LCD(myParent, this, deviceType, serialNum, hubPort, chNum);
				break;

			default:
				System.out.println("Device type " + deviceType + " not supported yet");
				myParent.exit();
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
	 * register real time events if defined by user
	 * 
	 */
	public void pre() {
		device.pre();
	}

	/**
	 * run events in the end of sketch's void draw(), in order to allow drawing inside event functions
	 * 
	 */
	public void draw() {
		device.draw();
	}
	
	/**
	 * close the Phidget channel object
	 * 
	 */
	public void dispose() {
		if (device != null ) {
			device.close();
		}
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

