/*
 The Sharp IR distance sensors series use simple sensor (analog) channels, but allow to set the specific sensor type (e.g. 2Y0A02)
  for automatic calculation of distance in centimeters.
  You can use getSensorValue for getting the distance in centimeters (as a float value).
  For detecting vlaues that are out of range (too close or too far), you can use getSensorValueValidity. See example Advanced -> Multimple_Sensors_Events
*/

import shenkar.SimplePhidgets.*;

Channel myDistanceSensor;

void setup() {
  size(600, 200);
  myDistanceSensor = new Channel(this, "1101");
  myDistanceSensor.setSensorType("2Y0A02");  // using Sharp distance sensor 20-150cm
}

void draw() {
  background(0);
  float dist = myDistanceSensor.getSensorValue();
  rect (300+dist, 50, 10, 100);
  rect (300-dist, 50, -10, 100);
}
