/*
 This example shows simple use of a Phidget RFID board
 */

import shenkar.SimplePhidgets.*;

Channel myRFID;

void setup() {
  size(400, 400);
  // using a "1024" Phidget RFID board, connected directly to USB
  myRFID = new Channel(this, "1024");
}

void draw() {
  background(0);

  // check if a tag is present
  if (myRFID.read() == 1) {
    strokeWeight(5);
    stroke(255);
    // check if the present tag is a specific one. This string can be obtained using the Phidgets Control Panel or by printing getLasTagString().
    if (myRFID.getLastTagString().equals("4d004a82a0")) {
      fill(255, 255, 0);
    } else {
      noFill();
    }
    rect (100, 100, 200, 200);
  }
}

/*
 All functions for RFID channel:
 
 read() - most basic way to use the channel. returns 0 if no tag present, or 1 if there is (int)
 getTagPresent() - same as "read()", but returns a boolean (true or false)
 getLastTagString() - gets the most recently read tag's data (String), even if that tag is no longer within read range. If called before any tag was read - returns an error
 getLastTagProtocol() - gets the most recently read tag's protocol (EM4100, ISO11785 FDX B or PhidgetTAG). returns a String.
 write(String tagString, String protocol, boolean lock) - writes data to the tag being currently read by the reader. You cannot write to a read-only or locked tag.
 getAntennaEnabled() - get the on/off state of the antenna. It must be on in order to detect and read tags. (boolean)
 setAntennaEnabled(boolean ant) - set the antenna state. You can turn it off when not used for reducing power consumption.
 
 Event functions:
 
 void tag() - called when the channel detects a tag.
 void tagLost() - called when the tag is not detected anymore.
 void tagRT() - real-time version of the event. See https://github.com/sgeigers/Phidgets4Processing
 void tagLostRT() - real-time version of the event. See https://github.com/sgeigers/Phidgets4Processing
 
 */
