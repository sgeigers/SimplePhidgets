/*
 The Sonar Phidget is an ultrasonic distance sensor with range of 40mm to ~10m
 For basic use - get distance value in millimeters using the getDistance() function.
 You can also use it as most other sensors using the read() function. In this case, you'll get an integer between 0 and 1000 proportional to minimum and maximum 
  distances of the sensor.
 More advanced users can use the getSonarReflections() functions to read distances of up to 8 object simultaneously.
*/

import shenkar.SimplePhidgets.*;

Channel mySonarSensor;
int maxDist;

void setup() {
  size(200, 900);
  mySonarSensor = new Channel(this, "DST1200");
  maxDist = mySonarSensor.getMaxDistance();
  
  fill(255);
  noStroke();
}

void draw() {
  background(0);
  int dist = mySonarSensor.getDistance();   // get distance in millimeters
 // println(dist);
  
  float y = map (dist, 0, maxDist, 850, 50);  // map readings of the sensor from 0..10000 millimeters range to y location on the window
  ellipse(100, y, 50, 50);
}

/*
 All functions for DistanceSensor channel (both sonar and the DST100x family of ToF optical sensors):
 
 read() - returns distance in arbitrary units (0..1000, int)
 getDistance() - returns distance in millimeters (int)
 
 getMinDistance() - returns minimum distance the sensor can measure
 getMaxDistance() - returns minimum distance the sensor can measure
 
 getSensorValue() - return distance in millimiters but as a float (given for compatibility with other sensors)
 getSensorUnit() - returns measurement units as String ("millimeters" - given for compatibility with other sensors)
 getSensorValueValidity() - returns true when measured distance is within sensor's range (can be used, e.g., for detecting there is no-one in-front of the sensor)
 
 setSonarQuietMode(boolean) - when set to true, the device will operate more quietly (with reduced range)
 getSonarQuietMode() - returns state of quiet mode
  
 Event functions:
 
 void distanceChange() - called when the channel detects a change in distance.
 void sonarReflectionsChange() - called when the channel updates the reflections data (see below)
 
 void distanceChangeRT() - real-time version of the event.
 void sonarReflectionsChangeRT() - real-time version of the event.

 setDistanceChangeTrigger(int) - the event is triggered only when at least the selected change in distance is detected by the sensor (in millimeters)
 getDistanceChangeTrigger() - returns minimum change in distance for triggering the event in millimeters
 getMinDistanceChangeTrigger() - minimum value for trigger
 getMaxDistanceChangeTrigger() - maximum value for trigger
 getDataInterval() - returns minimum interval in milliseconds between event triggers
 setDataInterval(int) - allows to set minimum interval in milliseconds between event triggers
 getMinDataInterval() - return minimum possible data interval for opened device
 getMaxDataInterval() - return maximum possible data interval for opened device

 Advanced functions:

 getSonarReflections()- returns u to 8 sonar reflections as an object of type com.phidget22.DistanceSensorSonarReflections. This can be useful, e.g., for choosing the closest detected object instead of the largest one
   for full information about dta structure, see https://www.phidgets.com/?view=api&lang=Java&api=DistanceSensor
   Example of use:
    com.phidget22.DistanceSensorSonarReflections reflections = mySonarSensor.getSonarReflections();
    if (reflections.count > 0) {
      println(reflections.distances[0]);  // print closest reflection's distance
    }

 
 */
