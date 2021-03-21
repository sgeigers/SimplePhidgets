/*
 This example demostrates the use of events when connecting a digital input to a hub. 
 The use of the "stateChange" event is similar to the use of "keyPressed" with one difference:
 The "stateChange" event will be called both when the button is pressed and when it is depressed. Ths means, if you want to detect only a press, you should check its state inside the event,
  as show below in the code.
 If you have multiple buttons (or switches etc.) and you need to know which one triggered the event, you should use stateChange(Channel ch) - see example Multiple_Sensors_Events
*/

import shenkar.SimplePhidgets.*;

Channel myButton;

void setup() {
  size(200, 400);
  // using a VINT hub port 2 as digital input: connect button terminals to "data" and "ground" pins of the port 
  myButton = new Channel(this, "HUB0000", 2, "digitalInput");
  background(0);
}

void draw() {
}
 
void stateChange() {
  if (myButton.read() == 1) {
    ellipse (100, 200, 100, 100);
  }
  else {
    background(0);
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
