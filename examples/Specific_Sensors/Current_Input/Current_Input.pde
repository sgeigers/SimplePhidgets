/*
 This example uses a servo controller (1061) to demonstrate the use of current input channel.
 The motor will move continuously and the current displayed as text (in console) as well as graphically.
*/

import shenkar.SimplePhidgets.*;

Channel myServo;
Channel myCurrentInput;

void setup() {
  size(100, 300);
  myServo = new Channel(this, "1061");  // open a channel for servo on channel 0 of the servo controller
  myCurrentInput = new Channel(this, "1061", "currentInput"); // open a secondary channel for current input
  
  noStroke();
}

void draw() {
  background(0);
  myServo.setAngle(sin(((float)millis())/500.0)*60 + 90);  // rotating the servo continuously with a sinusoidal movement
  
  float val = myCurrentInput.getCurrent();     // reading the current accurately. using the read() function will give a very low resolution reading (0, 1, 2 Amperes etc.), which isn't sufficient for this example.
  println(val);

  int colorVal = min((int)val*120, 255);  // using the current to set the color
  fill(colorVal, 255-colorVal, 0);        // setting the color to be green for low current, and go to red for high current
  rect(0,300,100,-(val*140+10));               // drawing a rect that will grow upwards as the current grows
}
 
/*
 All functions for current input channel:
 
 read() - most basic way to use the channel, BUT a very inaccurate one. returns current int Amperes (int). For getting a more accurate reading, use getCurrent() instead (see below)
 getCurrent() - returns measured current in Amperes (float)
 getMinCurrent() - the minimum current the board will report in Amperes (float)
 getMaxCurrent() - the minimum current the board will report in Amperes (float)
 getCurrentValidity() - can be used for validating the measured value (will return true when measured value is outside board's measuring limits) (boolean)
 
 Event functions:

 void currentChange() - called when the channel detects a change in current.
 void currentChangeRT() - real-time version of the event.

 setCurrentChangeTrigger(float) - the event is triggered only when at least the selected change of current is detected (in Amperes)
 getCurrentChangeTrigger() - returns minimum change for triggering the event in Amperes (float)
 getMinCurrentChangeTrigger() - minimum value for change trigger
 getMaxCurrentChangeTrigger() - maximum value for change trigger
 getDataInterval() - returns current minimum data interval between event calls (int)
 setDataInterval(int) - allows to set minimum interval in milliseconds between event triggers
 getMinDataInterval() - return minimum possible data interval for opened device
 getMaxDataInterval() - return maximum possible data interval for opened device

 Specipic functions for using with DAQ1400 - Versatile Input Phidget:
 
 getPowerSupply() - for some devices, returns power supply in Volts: 12, 24 or 0 for OFF
 setPowerSupply(int) - for some devices, sets power supply in Volts: 12, 24 or 0 for OFF

*/
