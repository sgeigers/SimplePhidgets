package shenkar.SimplePhidgets;

import processing.core.*;
import com.phidget22.*;

public class P_Digital_Output extends Device {
	public P_Digital_Output(PApplet P5Parent, Channel ChParent, String type, int serialNum, int portNum, int chNum) {
		super(P5Parent, ChParent, type, serialNum, portNum, chNum);

		// pre-opening setup
		try {
			device = new DigitalOutput();
		}	catch (PhidgetException ex) {
			System.err.println("Could not open device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
		}

		// device opening
		switch (deviceType) {
		case "1010": // PhidgetInterfaceKit 8/8/8
		case "1011": // PhidgetInterfaceKit 2/2/2
		case "1012": // PhidgetInterfaceKit 0/16/16
		case "1013": // PhidgetInterfaceKit 8/8/8
		case "1014": // PhidgetInterfaceKit 0/0/4 (Relays)
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
			initNoHub();
			break;

		case "LED1000": // 32x Isolated LED Phidget
		case "OUT1100": // 4x Digital Output Phidget
		case "REL1000": // 4x Relay Phidget
		case "REL1100": // 4x Isolated Solid State Relay Phidget
		case "REL1101": // 16x Isolated Solid State Relay Phidget
			init(false);
			break;

		case "HUB0000": // 6-Port USB VINT Hub Phidget
		case "HUB5000": // 6-Port Network VINT Hub Phidget
		case "SBC3003": // PhidgetSBC4 - 6-Port VINT Hub Phidget
		default:
			init(true);
			break;
		}

		// post-opening setup
	}

	/*
	 * most basic way to use the channel. Turns output to ON state
	 * 
	 */
	@Override
	public void on() {
		try {
			((DigitalOutput)device).setState(true);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set device " + deviceType + " to ON state because of error: " + ex);
		}
	}

	/*
	 * most basic way to use the channel. Turns output to OFF state
	 * 
	 */
	@Override
	public void off() {
		try {
			((DigitalOutput)device).setState(false);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set device " + deviceType + " to OFF state because of error: " + ex);
		}
	}

	/*
	 * set intensity of the output (duty cycle) between 0 and 1000
	 * 
	 * @param dutyCycle duty cycle (0..1000)
	 */
	@Override
	public void analogWrite(int dutyCycle) {  // 0..1000
		try {
			if (((DigitalOutput)device).getChannelSubclass() != ChannelSubclass.NONE) {
				if (dutyCycle<0) dutyCycle=0;
				if (dutyCycle>1000) dutyCycle = 1000;
				((DigitalOutput)device).setDutyCycle(((double)dutyCycle)/1000.0);
			}
			else {
				System.err.println("analogWrite(int) is not valid for device of type " + deviceType);				
			}
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot analog write for device " + deviceType + " because of error: " + ex);
		}
	}

	/*
	 * The DutyCycle represents the fraction of time the output is on (high).
	 * 
	 * @return float
	 */
	@Override
	public float getDutyCycle() {
		try {
			if (((DigitalOutput)device).getChannelSubclass() != ChannelSubclass.NONE) {
				return (float)(((DigitalOutput)device).getDutyCycle());
			}
			else {
				System.err.println("getDutyCycle() is not valid for device of type " + deviceType);				
			}
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get duty cycle from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	/*
	 * The DutyCycle represents the fraction of time the output is on (high).
	 * 
	 * @param dutyCycle duty cycle in percentage(%)
	 */
	@Override
	public void setDutyCycle(float dutyCycle) {
		try {
			if (((DigitalOutput)device).getChannelSubclass() != ChannelSubclass.NONE) {
				((DigitalOutput)device).setDutyCycle((double)dutyCycle);
			}
			else {
				System.err.println("setDutyCycle(float) is not valid for device of type " + deviceType);				
			}
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set duty cycle for device " + deviceType + " because of error: " + ex);
		}
	}

	/*
	 * The minimum value that DutyCycle can be set to.
	 * 
	 * @return float
	 */
	@Override
	public float getMinDutyCycle() {
		try {
			if (((DigitalOutput)device).getChannelSubclass() != ChannelSubclass.NONE) {
				return (float)(((DigitalOutput)device).getMinDutyCycle());
			}
			else {
				System.err.println("getMinDutyCycle() is not valid for device of type " + deviceType);				
			}
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min duty cycle from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	/*
	 * The maximum value that DutyCycle can be set to.
	 * 
	 * @return float
	 */
	@Override
	public float getMaxDutyCycle() {
		try {
			if (((DigitalOutput)device).getChannelSubclass() != ChannelSubclass.NONE) {
				return (float)(((DigitalOutput)device).getMaxDutyCycle());
			}
			else {
				System.err.println("getMaxDutyCycle() is not valid for device of type " + deviceType);				
			}
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max duty cycle from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	/*
	 * enables the failsafe feature for the channel, with a given failsafe time
	 * 
	 * @param failsafeTime time to enter fail safe mode (milliseconds)
	 */
	@Override
	public void enableFailsafe(int failsafeTime) {
		// applicable for newer relay and digital output boards only
		String sType = deviceType.substring(0, 3);
		if ((sType == "OUT") || (sType == "REL")) {
			try {
				((DigitalOutput)device).enableFailsafe(failsafeTime);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set failsafe for device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("enableFailsafe(int) is not valid for device of type " + deviceType);				
		}
	}	

	/*
	 * The minimum value that failsafe time can be set to.
	 * 
	 * @return int
	 */
	@Override
	public int getMinFailsafeTime() {
		// applicable for newer relay and digital output boards only
		String sType = deviceType.substring(0, 3);
		if ((sType == "OUT") || (sType == "REL")) {
			try {
				return ((DigitalOutput)device).getMinFailsafeTime();
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get min falesafe time from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getMinFailsafeTime() is not valid for device of type " + deviceType);				
		}
		return 0;
	}

	/*
	 * The maximum value that failsafe time can be set to.
	 * 
	 * @return int
	 */
	@Override
	public int getMaxFailsafeTime() {
		// applicable for newer relay and digital output boards only
		String sType = deviceType.substring(0, 3);
		if ((sType == "OUT") || (sType == "REL")) {
			try {
				return ((DigitalOutput)device).getMaxFailsafeTime();
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get max falesafe time from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getMaxFailsafeTime() is not valid for device of type " + deviceType);				
		}
		return 0;
	}

	/*
	 * resets the failsafe timer
	 * 
	 */
	@Override
	public void resetFailsafe() {
		// applicable for newer relay and digital output boards only
		String sType = deviceType.substring(0, 3);
		if ((sType == "OUT") || (sType == "REL")) {
			try {
				((DigitalOutput)device).resetFailsafe();
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot reset failsafe timer for device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("resetFailsafe() is not valid for device of type " + deviceType);				
		}
	}	

	/*
	 * the Frequency parameter sets the PWM frequency for all frequency-settable PWM outputs on the board (in hertz - Hz)
	 * 
	 * @return float
	 */
	@Override
	public float getFrequency() {
		if ((deviceType == "OUT1100") || (deviceType == "REL1100")) {
			try {
				return (float)(((DigitalOutput)device).getFrequency());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get frequency from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getFrequency() is not valid for device of type " + deviceType);				
		}
		return 0;
	}

	/*
	 * the Frequency parameter sets the PWM frequency for all frequency-settable PWM outputs on the board (in hertz - Hz)
	 * 
	 * @param frequency PWM frequency in Hz
	 */
	@Override
	public void setFrequency(float frequency) {
		if ((deviceType == "OUT1100") || (deviceType == "REL1100")) {
			try {
				((DigitalOutput)device).setFrequency((double)frequency);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set frequency for device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("setFrequency(float) is not valid for device of type " + deviceType);				
		}
	}

	/*
	 * the minimum value that frequency can be set to (Hz)
	 * 
	 * @return float
	 */
	@Override
	public float getMinFrequency() {
		if ((deviceType == "OUT1100") || (deviceType == "REL1100")) {
			try {
				return (float)(((DigitalOutput)device).getMinFrequency());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get min frequency from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getMinFrequency() is not valid for device of type " + deviceType);				
		}
		return 0;
	}

	/*
	 * the maximum value that frequency can be set to (Hz)
	 * 
	 * @return float
	 */
	@Override
	public float getMaxFrequency() {
		if ((deviceType == "OUT1100") || (deviceType == "REL1100")) {
			try {
				return (float)(((DigitalOutput)device).getMaxFrequency());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get max frequency from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getMaxFrequency() is not valid for device of type " + deviceType);				
		}
		return 0;
	}

	/*
	 * the LEDCurrentLimit is the maximum amount of current that the controller will provide to the output (amperes - A)
	 * 
	 * @return float
	 */
	@Override
	public float getLEDCurrentLimit() {
		try {
			if (((DigitalOutput)device).getChannelSubclass() == ChannelSubclass.DIGITAL_OUTPUT_LEDDRIVER) {
				return (float)(((DigitalOutput)device).getLEDCurrentLimit());
			}
			else {
				System.err.println("getLEDCurrentLimit() is not valid for device of type " + deviceType);				
			}
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get LED current limit from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	/*
	 * the LEDCurrentLimit is the maximum amount of current that the controller will provide to the output (amperes - A)
	 * 
	 * @param LEDCurrentLimit current in amperes
	 */
	@Override
	public void setLEDCurrentLimit(float LEDCurrentLimit) {
		try {
			if (((DigitalOutput)device).getChannelSubclass() == ChannelSubclass.DIGITAL_OUTPUT_LEDDRIVER) {
				((DigitalOutput)device).setFrequency((double)LEDCurrentLimit);
			}
			else {
				System.err.println("setLEDCurrentLimit(float) is not valid for device of type " + deviceType);				
			}
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set LED current limit for device " + deviceType + " because of error: " + ex);
		}
	}

	/*
	 * the min value that LEDCurrentLimit can be set to (amperes - A)
	 * 
	 * @return float
	 */
	@Override
	public float getMinLEDCurrentLimit() {
		try {
			if (((DigitalOutput)device).getChannelSubclass() == ChannelSubclass.DIGITAL_OUTPUT_LEDDRIVER) {
				return (float)(((DigitalOutput)device).getMinLEDCurrentLimit());
			}
			else {
				System.err.println("getMinLEDCurrentLimit() is not valid for device of type " + deviceType);				
			}
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min LED current limit from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	/*
	 * the max value that LEDCurrentLimit can be set to (amperes - A)
	 * 
	 * @return float
	 */
	@Override
	public float getMaxLEDCurrentLimit() {
		try {
			if (((DigitalOutput)device).getChannelSubclass() == ChannelSubclass.DIGITAL_OUTPUT_LEDDRIVER) {
				return (float)(((DigitalOutput)device).getMaxLEDCurrentLimit());
			}
			else {
				System.err.println("getMaxLEDCurrentLimit() is not valid for device of type " + deviceType);				
			}
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max LED current limit from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	/*
	 * the LEDForwardVoltage is the voltage that will be available to your LED (volts - V)
	 * 
	 * @return String
	 */
	@Override
	public String getLEDForwardVoltage() {
		try {
			if (((DigitalOutput)device).getChannelSubclass() == ChannelSubclass.DIGITAL_OUTPUT_LEDDRIVER) {
				LEDForwardVoltage volts = (((DigitalOutput)device).getLEDForwardVoltage());
				switch (volts) {
				case VOLTS_1_7:
					return "1.7";
				case VOLTS_2_75:
					return "2.75";
				case VOLTS_3_2:
					return "3.2";
				case VOLTS_3_9:
					return "3.9";
				case VOLTS_4_0:
					return "4.0";
				case VOLTS_4_8:
					return "4.8";
				case VOLTS_5_0:
					return "5.0";
				case VOLTS_5_6:
					return "5.6";
				}
			}
			else {
				System.err.println("getLEDForwardVoltage() is not valid for device of type " + deviceType);				
			}		
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get LED forward voltage from device " + deviceType + " because of error: " + ex);
		}
		return "";
	}	

	/*
	 * the LEDForwardVoltage is the voltage that will be available to your LED (volts - V)
	 * only applicable values are: "1.7", "2.75", "3.2", "3.9", "4.0", "4.8", "5.0" or "5.6".
	 * 
	 * @param LEDForwardVoltage LED forward voltage as string
	 */
	@Override
	public void setLEDForwardVoltage(String LEDFV) {
		try { 
			if (((DigitalOutput)device).getChannelSubclass() == ChannelSubclass.DIGITAL_OUTPUT_LEDDRIVER) {
				LEDForwardVoltage volts = LEDForwardVoltage.VOLTS_1_7;
				switch (LEDFV) {
				case "1.7":
					break;
				case "2.75":
					volts = LEDForwardVoltage.VOLTS_2_75;
					break;
				case "3.2":
					volts = LEDForwardVoltage.VOLTS_3_2;
					break;
				case "3.9":
					volts = LEDForwardVoltage.VOLTS_3_9;
					break;
				case "4":
				case "4.0":
					volts = LEDForwardVoltage.VOLTS_4_0;
					break;
				case "4.8":
					volts = LEDForwardVoltage.VOLTS_4_8;
					break;
				case "5":
				case "5.0":
					volts = LEDForwardVoltage.VOLTS_5_0;
					break;
				case "5.6":
					volts = LEDForwardVoltage.VOLTS_5_6;
					break;
				default:
					System.err.println("Invalid forward voltage: " + LEDFV + ". Use only \"1.7\", \"2.75\", \"3.2\", \"3.9\", \"4.0\", \"4.8\", \"5.0\" or \"5.6\"");
					break;						
				}
				((DigitalOutput)device).setLEDForwardVoltage(volts);
			}
			else {
				System.err.println("setLEDForwardVoltage(String) is not valid for device of type " + deviceType);				
			}		
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set LED forward voltage for device " + deviceType);
		}
	}
}
