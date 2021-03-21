package shenkar.SimplePhidgets;

import processing.core.PApplet;
import com.phidget22.*;

abstract public class Device {
	PApplet PAppletParent;
	Channel ChannelParent;
	int serial;
	int hubPort;
	int channelNum;
	Phidget device;
	String deviceType;

	public Device (PApplet theParent, Channel ChParent, String type, int serialNum, int portNum, int chNum) {
		PAppletParent = theParent;
		ChannelParent = ChParent;
		serial = serialNum;
		hubPort = portNum;
		channelNum = chNum;
		deviceType = type;
	}

	public void init(boolean isHubPort) {
		try {
			device.setHubPort(hubPort);
			device.setIsHubPortDevice(isHubPort);
			if (channelNum > -1) device.setChannel(channelNum);
			if (serial > -1) device.setDeviceSerialNumber(serial);
			device.open(1000);
		}
		catch (PhidgetException e) {
			if ((isHubPort) && (e.getErrorCode() == com.phidget22.ErrorCode.TIMEOUT)) {
				// if the device is connected to an "old" interface kit, it may have failed the opening procedure, so trying again without "isHubPort"
				try {
					device.setIsHubPortDevice(false);
					device.setHubPort(0);
					device.setChannel(hubPort);
					device.open(1000);
				}
				catch (PhidgetException ex) {
					System.err.println("Could not open device " + deviceType + ". See github.com/sgeigers/SimplePhidgets#reference for help");				
				}
			}
			else {
				System.err.println("Could not open device " + deviceType + ". See github.com/sgeigers/SimplePhidgets#reference for help.");
				System.err.println("Error: " + e);
			}
		}
	}

	public void initNoHub() {
		try {
			channelNum = hubPort;
			if (channelNum > -1) device.setChannel(channelNum);
			if (serial > -1) device.setDeviceSerialNumber(serial);
			device.open(1000);
		}
		catch (PhidgetException e) {
			System.err.println("Could not open device " + deviceType + ". See github.com/sgeigers/SimplePhidgets#reference for help");
			System.err.println("Error: " + e);
		}
	}
	
	public void close() {
		if (device != null) {
			try {
				device.close();
				System.out.println(deviceType + " Closed");
			}
			catch (PhidgetException ex) {
				System.err.println("Could not close device " + deviceType + ". See github.com/sgeigers/SimplePhidgets#reference for help");
			}
		}
	}

	public void draw() {
	}
	
	public int read() {
		System.err.println("read() is not valid for device of type " + deviceType);
		return 0;
	}

	public void on() {
		System.err.println("on() is not valid for device of type " + deviceType);
	}

	public void off() {
		System.err.println("off() is not valid for device of type " + deviceType);
	}

	public void analogWrite(int dutyCycle) {
		System.err.println("analogWrite(int) is not valid for device of type " + deviceType);
	}
	
	public boolean getBridgeEnabled() {
		System.err.println("getBridgeEnabled() is not valid for device of type " + deviceType);	
		return false;
	}

	public void setBridgeEnabled(boolean bridgeEnabled) {
		System.err.println("setBridgeEnabled(boolean) is not valid for device of type " + deviceType);	
	}

	public int getBridgeGain() {
		System.err.println("getBridgeGain() is not valid for device of type " + deviceType);	
		return 0;
	}

	public void setBridgeGain(int gain) {
		System.err.println("setBridgeGain(int) is not valid for device of type " + deviceType);			
	}

	public int getDataInterval() {
		System.err.println("getDataInterval() is not valid for device of type " + deviceType);	
		return 0;
	}

	public void setDataInterval(int dataInterval) {
		System.err.println("setDataInterval(int) is not valid for device of type " + deviceType);			
	}

	public int getMinDataInterval() {
		System.err.println("getMinDataInterval() is not valid for device of type " + deviceType);		
		return 0;
	}
	
	public int getMaxDataInterval() {
		System.err.println("getMaxDataInterval() is not valid for device of type " + deviceType);	
		return 0;
	} 

	public String getSensorType() {
		System.err.println("getSensorType() is not valid for device of type " + deviceType);	
		return "";
	}
	
	public void setSensorType(String sensorType) {
		System.err.println("setSensorType(String) is not valid for device of type " + deviceType);	
	}

	public String getSensorUnit() {
		System.err.println("getSensorUnit() is not valid for device of type " + deviceType);	
		return "";
	}

	public float getSensorValue() {
		System.err.println("getSensorValue() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public boolean getSensorValueValidity() {
		System.err.println("getSensorValueValidity() is not valid for device of type " + deviceType);	
		return false;
	}

	public float getSensorValueChangeTrigger() {
		System.err.println("getSensorValueChangeTrigger() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public void setSensorValueChangeTrigger(float sensorValueChangeTrigger) {
		System.err.println("setSensorValueChangeTrigger(float) is not valid for device of type " + deviceType);	
	}
	
	public float getVoltageRatio() {
		System.err.println("getVoltageRatio() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public float getMinVoltageRatio() {
		System.err.println("getMinVoltageRatio() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public float getMaxVoltageRatio() {
		System.err.println("getMaxVoltageRatio() is not valid for device of type " + deviceType);	
		return 0.0f;
	}
	
	public float getVoltageRatioChangeTrigger() {
		System.err.println("getVoltageRatioChangeTrigger() is not valid for device of type " + deviceType);	
		return 0.0f;
	}
	
	public void setVoltageRatioChangeTrigger(float voltageRatioChangeTrigger) {
		System.err.println("setVoltageRatioChangeTrigger(float) is not valid for device of type " + deviceType);	
	}

	public void setReadChangeTrigger(int readChangeTrigger) {
		System.err.println("setReadChangeTrigger(int) is not valid for device of type " + deviceType);	
	}

	public float getMinVoltageRatioChangeTrigger() {
		System.err.println("getMinVoltageRatioChangeTrigger() is not valid for device of type " + deviceType);	
		return 0.0f;
	}
	
	public float getMaxVoltageRatioChangeTrigger() {
		System.err.println("getMaxVoltageRatioChangeTrigger() is not valid for device of type " + deviceType);	
		return 0.0f;
	}
	
	public float getdB() {
		System.err.println("getdB() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public float getMaxdB() {
		System.err.println("getMaxdB() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public float getdBA() {
		System.err.println("getdBA() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public float getdBC() {
		System.err.println("getdBC() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public float getNoiseFloor() {
		System.err.println("getNoiseFloor() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public float[] getOctaves() {
		System.err.println("getOctaves() is not valid for device of type " + deviceType);	
		return null;
	}
	
	public float getSPLChangeTrigger() {
		System.err.println("getSPLChangeTrigger() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public void setSPLChangeTrigger(float SPLChangeTrigger) {
		System.err.println("setSPLChangeTrigger(float) is not valid for device of type " + deviceType);	
	}

	public float getMinSPLChangeTrigger() {
		System.err.println("getMinSPLChangeTrigger() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public float getMaxSPLChangeTrigger() {
		System.err.println("getMaxSPLChangeTrigger() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public int getSPLRange() {
		System.err.println("getSPLRange() is not valid for device of type " + deviceType);	
		return 0;
	}

	public void setSPLRange(int range) {
		System.err.println("setSPLRange(int) is not valid for device of type " + deviceType);	
	}

	public boolean getIsTouched() {
		System.err.println("getIsTouched() is not valid for device of type " + deviceType);	
		return false;
	}

	public float getSensitivity() {
		System.err.println("getSensitivity() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public void setSensitivity(float sensitivity) {
		System.err.println("setSensitivity(float) is not valid for device of type " + deviceType);	
	}

	public float getMinSensitivity() {
		System.err.println("getMinSensitivity() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public float getMaxSensitivity() {
		System.err.println("getMaxSensitivity() is not valid for device of type " + deviceType);	
		return 0.0f;
	}
	
	public float getTouchValue() {
		System.err.println("getSensorValue() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public float getMinTouchValue() {
		System.err.println("getMinTouchValue() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public float getMaxTouchValue() {
		System.err.println("getMaxTouchValue() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public float getTouchValueChangeTrigger() {
		System.err.println("getTouchValueChangeTrigger() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public void setTouchValueChangeTrigger(float touchValueChangeTrigger) {
		System.err.println("setTouchValueChangeTrigger(float) is not valid for device of type " + deviceType);	
	}

	public float getMinTouchValueChangeTrigger() {
		System.err.println("getMinTouchValueChangeTrigger() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public float getMaxTouchValueChangeTrigger() {
		System.err.println("getMaxTouchValueChangeTrigger() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public int getPowerSupply() { 
		System.err.println("getPowerSupply() is not valid for device of type " + deviceType);	
		return 0;
	}
	
	public void setPowerSupply(int ps) {
		System.err.println("setPowerSupply(int) is not valid for device of type " + deviceType);	
	}

	public float getVoltage() {
		System.err.println("getVoltage() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public float getMinVoltage() {
		System.err.println("getMinVoltage() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public float getMaxVoltage() {
		System.err.println("getMaxVoltage() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public float getVoltageChangeTrigger() {
		System.err.println("getVoltageChangeTrigger() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public void setVoltageChangeTrigger(float voltageChangeTrigger) {
		System.err.println("setVoltageChangeTrigger(float) is not valid for device of type " + deviceType);	
	}

	public float getMinVoltageChangeTrigger() {
		System.err.println("getMinVoltageChangeTrigger() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public float getMaxVoltageChangeTrigger() {
		System.err.println("getMaxVoltageChangeTrigger() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public int getVoltageRange() {
		System.err.println("getVoltageRange() is not valid for device of type " + deviceType);	
		return 0;
	}

	public void setVoltageRange(int vr) {
		System.err.println("setVoltageRange(int) is not valid for device of type " + deviceType);	
	}

	public float getRoll() {
		System.err.println("getRoll() is not valid for device of type " + deviceType);	
		return 0.0f;
	}
	
	public float getPitch() {
		System.err.println("getPitch() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public float getYaw() {
		System.err.println("getYaw() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public float[] getQuaternion() {
		System.err.println("getQuaternion() is not valid for device of type " + deviceType);	
		return new float[4];		
	}	
	
	public float[] getAccelerationArray() {
		System.err.println("getAccelerationArray() is not valid for device of type " + deviceType);	
		return new float[3];
	}

	public float[] getMinAccelerationArray() {
		System.err.println("getMinAccelerationArray() is not valid for device of type " + deviceType);	
		return new float[3];
	}

	public float[] getMaxAccelerationArray() {
		System.err.println("getMaxAccelerationArray() is not valid for device of type " + deviceType);	
		return new float[3];
	}

	public float[] getAngularRate() {
		System.err.println("getAngularRate() is not valid for device of type " + deviceType);	
		return new float[3];
	}

	public float[] getMinAngularRate() {
		System.err.println("getMinAngularRate() is not valid for device of type " + deviceType);	
		return new float[3];
	}

	public float[] getMaxAngularRate() {
		System.err.println("getMaxAngularRate() is not valid for device of type " + deviceType);	
		return new float[3];
	}

	public float[] getMagneticField() {
		System.err.println("getMagneticField() is not valid for device of type " + deviceType);	
		return new float[3];
	}

	public float[] getMinMagneticField() {
		System.err.println("getMinMagneticField() is not valid for device of type " + deviceType);	
		return new float[3];
	}

	public float[] getMaxMagneticField() {
		System.err.println("getMaxMagneticField() is not valid for device of type " + deviceType);	
		return new float[3];
	}

	public float getAccelerationChangeTrigger() {
		System.err.println("getAccelerationChangeTrigger() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public void setAccelerationChangeTrigger(float accelerationChangeTrigger) {
		System.err.println("setAccelerationChangeTrigger(float) is not valid for device of type " + deviceType);	
	}

	public float getMinAccelerationChangeTrigger() {
		System.err.println("getMinAccelerationChangeTrigger() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public float getMaxAccelerationChangeTrigger() {
		System.err.println("getMaxAccelerationChangeTrigger() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public float getMagneticFieldChangeTrigger() {
		System.err.println("getMagneticFieldChangeTrigger() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public void setMagneticFieldChangeTrigger(float magneticFieldChangeTrigger) {
		System.err.println("setMagneticFieldChangeTrigger(float) is not valid for device of type " + deviceType);	
	}

	public float getMinMagneticFieldChangeTrigger() {
		System.err.println("getMinMagneticFieldChangeTrigger() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public float getMaxMagneticFieldChangeTrigger() {
		System.err.println("getMaxMagneticFieldChangeTrigger() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public int getAxisCount() {
		System.err.println("getAxisCount() is not valid for device of type " + deviceType);	
		return 0;
	}

	public float getTimestamp() {
		System.err.println("getTimestamp() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public void zeroGyro() {
		System.err.println("zeroGyro() is not valid for device of type " + deviceType);	
	}

	public void setMagnetometerCorrectionParameters(float magneticField, float offset0,	float offset1, float offset2, float gain0, float gain1, float gain2, float T0, float T1, float T2, float T3, float T4, float T5) {
		System.err.println("setMagnetometerCorrectionParameters(float, float, float , ...) is not valid for device of type " + deviceType);	
	}

	public void resetMagnetometerCorrectionParameters() {
		System.err.println("resetMagnetometerCorrectionParameters() is not valid for device of type " + deviceType);	
	}

	public void saveMagnetometerCorrectionParameters() {
		System.err.println("saveMagnetometerCorrectionParameters() is not valid for device of type " + deviceType);	
	}

	public String getAlgorithm() {
		System.err.println("getAlgorithm() is not valid for device of type " + deviceType);	
		return "";
	}

	public void zeroAlgorithm() {
		System.err.println("zeroAlgorithm() is not valid for device of type " + deviceType);	
	}

	public void setAlgorithm(String alg) {
		System.err.println("setAlgorithm(String) is not valid for device of type " + deviceType);	
	}
	
	public String getInputMode() {
		System.err.println("getInputMode() is not valid for device of type " + deviceType);	
		return "";
	}

	public void setInputMode(String im) {
		System.err.println("setInputMode(String) is not valid for device of type " + deviceType);	
	}
	
	public boolean getState() {
		System.err.println("getState() is not valid for device of type " + deviceType);	
		return false;
	}

	public float getDutyCycle() {
		System.err.println("getDutyCycle() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public void setDutyCycle(float dutyCycle) {
		System.err.println("setDutyCycle(float) is not valid for device of type " + deviceType);	
	}


	public float getMinDutyCycle() {
		System.err.println("getMinDutyCycle() is not valid for device of type " + deviceType);	
		return 0.0f;
	}
	
	public float getMaxDutyCycle() {
		System.err.println("getMaxDutyCycle() is not valid for device of type " + deviceType);	
		return 0.0f;
	}
	
	public void enableFailsafe(int failsafeTime) {
		System.err.println("enableFailsafe(int) is not valid for device of type " + deviceType);	
	}	
	
	public int getMinFailsafeTime() {
		System.err.println("getMinFailsafeTime() is not valid for device of type " + deviceType);	
		return 0;
	}

	public int getMaxFailsafeTime() {
		System.err.println("getMaxFailsafeTime() is not valid for device of type " + deviceType);	
		return 0;
	}

	public void resetFailsafe() {
		System.err.println("resetFailsafe() is not valid for device of type " + deviceType);	
	}	

	public float getFrequency() {
		System.err.println("getFrequency() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public void setFrequency(float frequency) {
		System.err.println("setFrequency(float) is not valid for device of type " + deviceType);	
	}

	public float getMinFrequency() {
		System.err.println("getMinFrequency() is not valid for device of type " + deviceType);	
		return 0.0f;
	}
	
	public float getMaxFrequency() {
		System.err.println("getMaxFrequency() is not valid for device of type " + deviceType);	
		return 0.0f;
	}
	
	public float getLEDCurrentLimit() {
		System.err.println("getLEDCurrentLimit() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public void setLEDCurrentLimit(float LEDCurrentLimit) {
		System.err.println("setLEDCurrentLimit(float) is not valid for device of type " + deviceType);	
	}

	public float getMinLEDCurrentLimit() {
		System.err.println("getMinLEDCurrentLimit() is not valid for device of type " + deviceType);	
		return 0.0f;
	}

	public float getMaxLEDCurrentLimit() {
		System.err.println("getMaxLEDCurrentLimit() is not valid for device of type " + deviceType);	
		return 0.0f;
	}
	
	public String getLEDForwardVoltage() {
		System.err.println("getLEDForwardVoltage() is not valid for device of type " + deviceType);	
		return "";
	}	
	
	public void setLEDForwardVoltage(String LEDFV) {
		System.err.println("setLEDForwardVoltage(String) is not valid for device of type " + deviceType);	
	}

	public void setAngle(int ang) {
		System.err.println("setAngle(int) is not valid for device of type " + deviceType);	
	}

	public void setAngle(float ang) {
		System.err.println("setAngle(float) is not valid for device of type " + deviceType);	
	}

	public float getAcceleration() {
		System.err.println("getAcceleration() is not valid for device of type " + deviceType);	
		return 0;
	}

	public void setAcceleration(float accel) {
		System.err.println("setAcceleration(float) is not valid for device of type " + deviceType);	
	}

	public float getMinAcceleration() {
		System.err.println("getMinAcceleration() is not valid for device of type " + deviceType);	
		return 0;
	}

	public float getMaxAcceleration() {
		System.err.println("getMaxAcceleration() is not valid for device of type " + deviceType);	
		return 0;
	}

	public boolean getEngaged() {
		System.err.println("getEngaged() is not valid for device of type " + deviceType);	
		return false;
	}

	public void setEngaged(boolean eng) {
		System.err.println("setEngaged(boolean) is not valid for device of type " + deviceType);	
	}
	public boolean getIsMoving() {
		System.err.println("getIsMoving() is not valid for device of type " + deviceType);	
		return false;
	}

	public float getPosition() {
		System.err.println("getPosition() is not valid for device of type " + deviceType);	
		return 0;
	}

	public void setMinPosition(float pos) {
		System.err.println("setMinPosition(float) is not valid for device of type " + deviceType);	
	}

	public float getMinPosition() {
		System.err.println("getMinPosition() is not valid for device of type " + deviceType);	
		return 0;
	}

	public void setMaxPosition(float pos) {
		System.err.println("setMaxPosition(float) is not valid for device of type " + deviceType);	
	}

	public float getMaxPosition() {
		System.err.println("getMaxPosition() is not valid for device of type " + deviceType);	
		return 0;
	}

	public void setMinPulseWidth(float pls) {
		System.err.println("setMinPulseWidth(float) is not valid for device of type " + deviceType);	
	}

	public float getMinPulseWidth() {
		System.err.println("getMinPulseWidth() is not valid for device of type " + deviceType);	
		return 0;
	}

	public void setMaxPulseWidth(float pls) {
		System.err.println("setMinPulseWidth(float) is not valid for device of type " + deviceType);	
	}

	public float getMaxPulseWidth() {
		System.err.println("getMaxPulseWidth() is not valid for device of type " + deviceType);	
		return 0;
	}

	public float getMinPulseWidthLimit() {
		System.err.println("getMinPulseWidthLimit() is not valid for device of type " + deviceType);	
		return 0;
	}
	
	public float getMaxPulseWidthLimit() {
		System.err.println("getMaxPulseWidthLimit() is not valid for device of type " + deviceType);	
		return 0;
	}

	public boolean getSpeedRampingState() {
		System.err.println("getSpeedRampingState() is not valid for device of type " + deviceType);	
		return false;
	}

	public void setSpeedRampingState(boolean state) {
		System.err.println("setSpeedRampingState(boolean) is not valid for device of type " + deviceType);	
	}

	public float getTargetPosition() {
		System.err.println("getTargetPosition() is not valid for device of type " + deviceType);	
		return 0;
	}

	public void setTargetPosition(float tgt) {
		System.err.println("setTargetPosition(float) is not valid for device of type " + deviceType);	
	}
	
	public float getTorque() {
		System.err.println("getTorque() is not valid for device of type " + deviceType);	
		return 0;
	}

	public void setTorque(float trq) {
		System.err.println("setTorque(float) is not valid for device of type " + deviceType);	
	}

	public float getMinTorque() {
		System.err.println("getMinTorque() is not valid for device of type " + deviceType);	
		return 0;
	}

	public float getMaxTorque() {
		System.err.println("getMaxTorque() is not valid for device of type " + deviceType);	
		return 0;
	}
	
	public float getVelocity() {
		System.err.println("getVelocity() is not valid for device of type " + deviceType);	
		return 0;
	}
	
	public float getVelocityLimit() {
		System.err.println("getVelocityLimit() is not valid for device of type " + deviceType);	
		return 0;
	}
	
	public void setVelocityLimit(float vel) {
		System.err.println("setVelocityLimit(float) is not valid for device of type " + deviceType);	
	}
	
	public float getMinVelocityLimit() {
		System.err.println("getMinVelocityLimit() is not valid for device of type " + deviceType);	
		return 0;
	}
	
	public float getMaxVelocityLimit() {
		System.err.println("getMaxVelocityLimit() is not valid for device of type " + deviceType);	
		return 0;
	}
	
	public String getVoltageString() {
		System.err.println("getVoltageString() is not valid for device of type " + deviceType);	
		return "";
	}

	public void setVoltage(String vol) {
		System.err.println("setVoltage(String) is not valid for device of type " + deviceType);	
	}
}

