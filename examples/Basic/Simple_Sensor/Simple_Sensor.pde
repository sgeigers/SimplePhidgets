/*
 This example demonstrates the use of a simple sensor (e.g. a light sensor) connected to a VINT hub or an interfaceKit.
 The value of the sensor is read and used for setting the circles's fill color.
 See comments and explanations inside code, and full function descriptions below.
*/

import shenkar.SimplePhidgets.*;

Channel mySimpleSensor;

void setup() {
  size(400, 400);
  mySimpleSensor = new Channel(this, "1142");  // opening a channel for the sensor, using it's part number (4 digits or 3 letterrs and 4 digits, e.g "LUX1000").
}

void draw() {
  background (0);
  int val = mySimpleSensor.read();  // get sensor value between 0 and 1000
  println (val);
  noStroke();
  fill (val/4, val/4, val/6);  // setting fill color proportional to sensor value. the dividers were set by trial and error
  ellipse (200, 200, 300, 300);
}

/*
 Like this example, the most basic function to use with sensors is "read()", which returns the current value of the sensor as an integer
  number between 0 and 1000.
 
 There are some Phidget boards that require different definitions (e.g HIN1100 Thumbstick, which have x and y axes as well as a button).
  Most of these boards have their own examples in the Specific_Sensors examples folder.
  
 In addition, there are more advanced functions that let you get the most out of your sensors. You are welcome to explore the
  Advanced_Concepts examples folder to find out more about these functions.
  
 Specifically, it is recommended to explore these examples:

 Advanced_Concepts -> Simple_Sensor_Event - demonstrates the use of event functions with your sensors (similar to proceing's keyPressed()).
 Advanced_Concepts -> Sensor_Full_Doc - Specifies all available functions for sensor boards, and demostrates the use of some of them.
*/
