/*
 This example shows the use of a current input channel with a DC motor controller board. This allows, for example, the processing sketch to "know" when the current consumption of the motor is high, which indicates
   high friction or motor stall.
 Not all DC motor controller boards have this option. currently, only these boards have it:
 1064 - PhidgetMotorControl HC
 1065 - PhidgetMotorControl 1-Motor
 DCC1000 - DC Motor Phidget
*/

import shenkar.SimplePhidgets.*;

Channel myDCMotor;
Channel myCurrentSensor;

void setup() {
  size(200, 200);
  myDCMotor = new Channel(this, "DCC1000");  // open a channel for a DC motor connected to the DC Motor Phidget (DCC1000), which is connected to channel 0 of a VINT hub
  myCurrentSensor = new Channel(this, "DCC1000", "currentInput");  // open a channel for current input channel of the board
  
  myDCMotor.setAcceleration(myDCMotor.getMaxAcceleration());  // setting maximum acceleration - to more clearly see peak currents when raising speed
  
  noFill();
}

void draw() {
  myDCMotor.setTargetVelocity(float(mouseX-100)/100.0);
  float current = myCurrentSensor.getSensorValue();
  println(current);

  background(0);
  stroke(255);
  rect(50,25,100,150);
  stroke(255,0,0);
  float y = 175 - abs(current)*100;
  line(50,y,150,y);
}
 
/*
 For full documentation of DCMotor controller see example Basic -> DC_Motor
 For full documentation of current input channel see example Specific_Sensors -> Current_Input

*/
