/*
 This example demonstrates the use of a PhidgetGPS (1040) board.
 The heading and velocity of motion will be displayed as a vector (so you need to move - walk - in order to see the effect...)
 */

import shenkar.SimplePhidgets.*;

Channel myGPS;

void setup() {
  size(400, 400);
  myGPS = new Channel(this, "1040");  // open a channel for the GPS board

  stroke(200, 180, 0);
  strokeWeight(3);
  strokeCap(ROUND);
}

void draw() {
  background(0);

  // check if a position fix has been achieved (otherwise - son't try and read data from GPS)

  if (myGPS.getPositionFixState()) {
    // get heading in degrees and velocity in km/h
    float heading = myGPS.getHeading();
    float velocity = myGPS.getVelocity();
    println("heading: "+heading+ "\tvelocity: "+velocity);

    // convert heading to radians and multiply velocity by 50
    heading *= DEG_TO_RAD;
    velocity *= 50;

    // move drawing matrix to center of window and rotate according to heading
    translate(width/2, height/2);
    rotate(heading);

    // draw arrow line upwards (rotated by "heading", so north is upwards)
    line(0, 0, 0, -velocity);

    // move drawing matrix to head of the line to draw arrow head
    translate(0, -velocity);

    // draw arrow head
    line(0, 0, -velocity/15, velocity/10);
    line(0, 0, velocity/15, velocity/10);
  }
  else {
    println ("No signal");
  }
}

/*
 All functions for GPS channel:
 
 getPositionFixState() - returns the status of the position fix. True if a fix is available and latitude, longitude, and altitude can be read. False if the fix is not available.
 getLatitude() - returns the latitude of the GPS (float)
 getLongitude() - returns the longitude of the GPS (float)
 getAltitude() - returns the altitude above mean sea level in meters (float)
 getHeading() - returns the current true course over ground of the GPS in degrees (float)
 getVelocity() - return the current speed over ground of the GPS in kilometers/hour (float)
 
 Event functions:
 
 void positionChange() - called when the channel detects a change in position.
 void headingChange() - called when the channel detects a change in heading direction.
 voi positionFixState() - called when the position fix status changes (see above)
 
 void positionChangeRT() - real-time version of the event.
 void headingChangeRT() - real-time version of the event.
 voi positionFixStateRT() - real-time version of the event.
 
 Other functions:
 
 getYear() - returns year (int)
 getMonth() - returns month (1-12, int)
 getDay() - returns day in month (1-31, int)
 getHours() - returns hours  (0-23, int)
 getMinutes() - returns minutes (0-59, int)
 getSeconds() - returns seconds (0-59, int)
 getMilliseconds() - retunrs milliseconds (int)
 
 Advanced functions:
 
 getDateAndTime() - returns a Java object of type java.util.Calendar with current date and time information
 getNMEAData()- returns NMEA data from satellite as an object of type com.phidget22.NMEAData. This can be useful to get more data, such as number of satellites in use, position fix quality etc.
   for full information about dta structure, see https://www.phidgets.com/?view=api&lang=Java&api=GPS
   Example of use:
    com.phidget22.NMEAData data = myGPS.getNMEAData();
    println(data.GGA.numSatellites);

 
 */
