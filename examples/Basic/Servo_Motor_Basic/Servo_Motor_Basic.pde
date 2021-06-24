/*
 This example shows simple use of a servo motor connected to a Phidget servo board.
 The motor will rotate to position set by the mouse location 
*/

import shenkar.SimplePhidgets.*;

Channel myServo;

void setup() {
  size(180, 200);
  myServo = new Channel(this, "RCC1000");  // open a channel for servo connected to channel 0 of the board
}

void draw() {
  myServo.setAngle(mouseX);
}
 
/*
 All functions for servo channel:
 
 setAngle(float) - most basic way to use the channel. Rotates the servo motor to selected position in degrees.

 setTargetPosition(float) - same as setAngle, but does not engage the motor and does not set velocity and acceleration (these are needed before engaging)
 getTargetPosition() - get target position as set in setAngle() or setTargetPosition().
 setMinPosition() - set the minimum angle for the servo, corresponded to minPulseWidth. see "*** Tuning paramenters for your servo" bellow.
 getMinPosition() - the minimum value that Angle and TargetPosition can be set to.
 setMaxPosition() - set the maximum angle for the servo, corresponded to minPulseWidth. see "*** Tuning paramenters for your servo" bellow.
 getMaxPosition() - the maximum value that Angle and TargetPosition can be set to.
 getPosition() - the most recent position value that the controller has reported.
 
 setMinPulseWidth(float) - set the minimum pulse width that your servo motor specifies (microseconds). see "*** Tuning paramenters for your servo" bellow.
 getMinPulseWidth() - get the minimum uplse width set for your servo.
 setMaxPulseWidth(float) - set the minimum pulse width that your servo motor specifies (microseconds). see "*** Tuning paramenters for your servo" bellow.
 getMaxPulseWidth() - get the maximum uplse width set for your servo.
 getMinPulseWidthLimit() - get the minimum pulse width that MinPulseWidth can be set to.
 getMaxPulseWidthLimit() - et the axnimum pulse width that MinPulseWidth can be set to.
 
 setVelocityLimit(float) - when moving, the servo motor velocity will be limited by this value (degrees per second)
 getMinVelocityLimit() - minimum value for VelosityLimit.
 getMaxVelocityLimit() - maximum value for VelosityLimit.
 getVelocity() - get the velocity that the servo motor is being driven at.
 getVelocityLimit() - as set in setVelocityLimit().

 getAcceleration() - get the rate at which the controller can change the motor's velocity (deg/sec^2)
 setAcceleration(float) - set the rate at which the controller can change the motor's velocity
 getMinAcceleration() - get the minimum value that acceleration can be set to.
 getMaxAcceleration() - get the maximum value that Acceleration can be set to.

 setSpeedRampingState(boolean) - only for some boards. when true the controller will take the Acceleration and Velocity properties into account when moving the servo motor,
     usually resulting in smooth motion. true by default.
 getSpeedRampingState() - get speed ramping state.
 
 getIsMoving() - only for some boards. true if the controller is sending steps to the motor (it can't indicate a stalled motor).
 
 getEngaged() - when engaged, a RC servo motor has the ability to be positioned. When disengaged, no commands are sent to the RC servo motor.
 setEngaged(boolean) - this is automatically set to true when calling setAngle
 
 getVoltageString() - the supply voltage for all servo motors connected to the servo controller (returns a String) (Volts)
 setVoltage(String) - set voltage for all servos connected to the controller. Can only be set to "5.0", "6.0" or "7.4" (Volts).
 
 Event functions:

 void positionChange() - called when the servo's position is changed, based on data rate set with setDataRate.
 void velocityChange() - called when the servo's velocity is changed, based on data rate set with setDataRate.
 void targetReached() - called when the servo has reached its target. Note: there is no feedback to the controller, so it does not know whether the motor actually reached its target.
   This actually indicates the controller stopped changing the position.
   
 void positionChangeRT() - the real-time version of above event (not bound to frame rate of the sketch)
 void velocityChangeRT() - the real-time version of above event (not bound to frame rate of the sketch)
 void targetReachedRT() - the real-time version of above event (not bound to frame rate of the sketch)
 
 getDataInterval() - returns minimum interval in milliseconds between event triggers
 setDataInterval(int) - allows to set minimum interval in milliseconds between event triggers
 getMinDataInterval() - return minimum possible data interval for opened device
 getMaxDataInterval() - return maximum possible data interval for opened device

 for newer servo boards (VINT RCServo controllers), these functions are also available:
 
 enableFailsafe(int) - enable the fail-safe mode with the given fail-safe time.
 getMinFailsafeTime() - returns min fale-safe time
 getMaxFailsafeTime() - returns max fale-safe time
 resetFailsafe() - resets the fail-safe timer. This need to be called periodically to prevent the board to enter fail-safe mode.
 
 *** Tuning paramenters for your servo:
 1. First adjust the servo's range of motion by setting the MaxPulseWidth and MinPulseWidth. You can use the default values for these (or the ones on your servo's datasheet)
     as a starting point.
 2. Send the servo to MaxPosition and MinPosition to check the results. Repeat steps 1 and 2 as nessesarry.
 3. Set the MaxPosition and MinPosition to match whatever numbers you find best suited to your application.

*/
