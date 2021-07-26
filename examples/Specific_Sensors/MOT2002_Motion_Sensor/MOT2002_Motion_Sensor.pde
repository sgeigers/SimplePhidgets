/*
 The MOT2002 is a PIR (Passive Infra-Red) sensor that changes its output voltage when it "sees" a movement of hot objects.
 You can use it, as most other sensors, with the "read" function. In this case, the "read" will return a value of ~500 when no motion is detected,
   and will change (to either direction) when motion is detected.
 Another option is to use the library algorithm for motion detection by using "getSensorValue". This will return 0 (float) when there is no motion and 1 (float) when
   motion is detected. Note though that the sensor needs a few seconds of no motion for calibration when running the sketch. While calibrating, a "Sensor value out of range"
   error will occur when reading sensor value.
   You can change the sensitivity of the algorithm by adding "setSensorType" with desired sensitivity (LOW, MED or HIGH) as shown in this example. Default sensitivity is MED.
*/

import shenkar.SimplePhidgets.*;

Channel myMotionSensor;

void setup() {
  size(400, 400);
  myMotionSensor = new Channel(this, "MOT2002");
  myMotionSensor.setSensorType("HIGH");  // setting high sensitivity. default is medium.
}

void draw() {
  background(0);
  float motion = myMotionSensor.getSensorValue();
  if (motion == 1.0) {
    fill(255,100,100);
    ellipse(200, 200, 300, 300);
  }
}
