/*
 This example shows simple use of an LED connected to a VINT hub.
 */

import shenkar.SimplePhidgets.*;

Channel myDigitalOutput;

void setup() {
  size(200, 200);
  // connect the LED + to port 0 DO (white) and LED - to port 0 Ground (black)
  myDigitalOutput = new Channel(this, "HUB0000", "digitalOutput");
  background(0);
}

void draw() {
  myDigitalOutput.on();
  delay(mouseX*5);
  myDigitalOutput.off();
  delay(mouseX*5);
}
 
/*
 All functions for digitalOutput channel:
 
 on() - turn the channel on.
 off() - turn the channel off.
 
 for newer boards, you can set the intensity of the channel (usually - used for LED lights):
 
 analogWrite(int) - set the channel intensity (0..1000). This actually changes the PWM duty cycle (you can read about it in the Phidgets website)
 getDutyCycle() - returns current duty cycle (float 0..1). represents the fraction of time the output is on (high).
 setDutyCycle(float) - same as analogWrite, but takes a float from 0 to 1.
 getMinDutyCycle() - returns min duty cycle possible for this channel
 getMaxDutyCycle() - returns max duty cycle possible for this channel
 
 LED boards can control the voltage and current for LEDs:
 
 getLEDCurrentLimit() - returns current limit set for the channel in amperes (float)
 setLEDCurrentLimit(float) - set current to LED channel (amperes - A)
 getMinLEDCurrentLimit() - returns min current limit for channel
 getMaxLEDCurrentLimit() - returns max current limit for channel
 getLEDForwardVoltage() - returns voltage available for all LEDs connected to board (String, in Volts)
 setLEDForwardVoltage(String) - set voltage for all LEDs on board. This can only be: "1.7", "2.75", "3.2", "3.9", "4.0", "4.8", "5.0" or "5.6"
 
 for newer relay and output boards (p/n starting with REL and OUT), a fail-safe function is available. read more in Phidgets website:
 
 enableFailsafe(int) - enable the fail-safe mode with the given fail-safe time.
 getMinFailsafeTime() -returns min fale-safe time
 getMaxFailsafeTime() -returns max fale-safe time
 resetFailsafe() - resets the fail-safe timer. This need to be called periodically to prevent the board to enter fail-safe mode.
 
 the OUT1100 and REL1100 allow to change the PWN frequency:
 
 getFrequency() - returns the PWM frequency of all channels (float)
 setFrequency(float) - set PWM frequency
 getMinFrequency()- returns min frequency for device
 getMaxFrequency()- returns max frequency for device

*/
