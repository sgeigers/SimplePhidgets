/*
 This example shows simple use of a button or switch connected to an interfaceKit. (It is similar to connecting it to a VINT hub - mostly the physical connections are different)
*/

import shenkar.SimplePhidgets.*;

Channel myButtonChannel;

void setup() {
  size(200, 400);
  // using a "1018" interfaceKit, connect a button between terminal 4 of INPUTS and G 
  myButtonChannel = new Channel(this, "1018", 4, "digitalInput");
}

void draw() {
  background(0);
  int val = myButtonChannel.read();  // get button value; 0 or 1
  if (val == 1) {
    ellipse (100, 200, 100, 100);
  }
}
 
/*
 All functions for digitalInput channel:
 
 read() - most basic way to use the channel. returns 0 or 1 (int)
 getState() - get state sensed by the device as boolean (true or false)
 
 Event functions:

 void stateChange() - called when the channel detects a change in state. See example Digital_Input_Event
 void stateChangeRT() - real-time version of the event. See https://github.com/sgeigers/Phidgets4Processing

 Specipic functions for using with DAQ1400 - Versatile Input Phidget:
 
 getInputMode() - for some devices, returns whether input mode is NPN or PNP. Returns a String (e.g. "PNP")
 setInputMode(String) - for some devices, sets whether input mode is NPN or PNP
 getPowerSupply() - for some devices, returns power supply in Volts: 12, 24 or 0 for OFF
 setPowerSupply(int) - for some devices, sets power supply in Volts: 12, 24 or 0 for OFF
 
*/
