/*
 This example demonstrates opening multiple sensors connected to 2 hubs.
*/

import shenkar.SimplePhidgets.*;

Channel myLightSensor;
Channel mySlider;
Channel mySoundSensor;

void setup() {
  size(400, 400);
  mySlider = new Channel(this, "1112", 619570, 3); // open a channel for 60mm slider 1112 on port 3 of hub w/ serial number 619570
  mySoundSensor = new Channel(this, "SND1000", 619570, 0);  // open a channel for sound phidget on port 0 of hub w/ serial number 619570. If we connect more than 1 sensor to a hub, it's better to specify the port number
  myLightSensor = new Channel(this, "1142", 561918); // open a channel for light sensor 1142 on port 0 of hub w/ serial number 561918

  rectMode(CENTER);
  noStroke();
}

void draw() {
  background(0);
  
  // reading sensors to float variables
  float snd = mySoundSensor.read();
  float sld = mySlider.read();
  float light = myLightSensor.read();
  
  // drawing a dancing "star"
  translate(width/2, height/2);
  for (int i=0; i<12; i++) {
    pushMatrix();
    translate (width/4,0);
    rotate(snd/500*PI);
    fill (light/4, 167, 134);
    rect (0, 0, sld/10, 5);
    popMatrix();
    rotate(PI/4);
  }
}
