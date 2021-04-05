/*
 This example shows simple use of a servo motor connected to a Phidget servo board.
 The motor will rotate to position set by the mouse location 
*/

import shenkar.SimplePhidgets.*;

Channel myServo;

void setup() {
  size(180, 200);
  myServo = new Channel(this, "RCC1000");  // open a channel for servo connected to channel 0 of the board
}

void draw() {
  myServo.setAngle(mouseX);
}
 
/*
 All functions for servo channel:
 
 read() - most basic way to use the channel. returns 0 or 1 (int)
 getState() - get state sensed by the device as boolean (true or false)
 
 Event functions:

 void positionChange() - called when the servo's position is changed, based on data rate set with setDataRate.
 void velocityChange() - called when the servo's velocity is changed, based on data rate set with setDataRate.
 void targetReached() - called when the servo reaches its target position.

 Specipic functions for using with DAQ1400 - Versatile Input Phidget:
 
 getInputMode() - for some devices, returns whether input mode is NPN or PNP. Returns a String (e.g. "PNP")
 setInputMode(String) - for some devices, sets whether input mode is NPN or PNP
 getPowerSupply() - for some devices, returns power supply in Volts: 12, 24 or 0 for OFF
 setPowerSupply(int) - for some devices, sets power supply in Volts: 12, 24 or 0 for OFF
 
*/
