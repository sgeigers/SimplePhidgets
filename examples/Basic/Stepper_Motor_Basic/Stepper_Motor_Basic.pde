/*
 This example shows simple use of a stepper motor connected to a Phidget stepper controller board.
 The motor will rotate to position set by the mouse location 
*/

import shenkar.SimplePhidgets.*;

Channel myStepper;

void setup() {
  size(180, 200);
  myStepper = new Channel(this, "STC1003");  // open a channel for a stepper connected to the stepper phidget, which is connected to channel 0 of a VINT hub
}

void draw() {
  myStepper.setTargetPosition(mouseX*50);
}
 
/*
 All functions for stepper channel:
 
 setTargetPosition(float) - most basic way to use the channel. Rotates the motor the selected position in 1/16 of steps.

 getTargetPosition() - get target position as set in setTargetPosition().
 getMinPosition() - the minimum value that TargetPosition can be set to.
 getMaxPosition() - the maximum value that TargetPosition can be set to.
 getPosition() - the most recent position value that the controller has reported.
 addPositionOffset(int) - adds an offset (positive or negative) to the current position and target position. This is especially useful for zeroing position.
  
 setVelocityLimit(float) - when moving, the stepper motor velocity will be limited by this value (units per second)
 getMinVelocityLimit() - minimum value for VelosityLimit.
 getMaxVelocityLimit() - maximum value for VelosityLimit.
 getVelocity() - get the most recent velocity value that the controller has reported.
 getVelocityLimit() - as set in setVelocityLimit().

 getAcceleration() - get the rate at which the controller can change the motor's velocity
 setAcceleration(float) - set the rate at which the controller can change the motor's velocity
 getMinAcceleration() - get the minimum value that acceleration can be set to.
 getMaxAcceleration() - get the maximum value that Acceleration can be set to.
 
 getIsMoving() - true if the controller is sending steps to the motor (it can't indicate a stalled motor)
 
 getControlMode() - "STEP" = controlling the motor with target position.  "RUN" = controlling with speed (continueous run)
 setControlMode(String) - set to "STEP" or "RUN"
 
 getCurrentLimit() - current limit to the motor (in Amperes)
 setCurrentLimit(float) - the current through the motor will be limited by the set value
 getMinCurrentLimit() - get minimum current imit to the motor
 getMaxCurrentLimit() - get maximum current limit to the motor
 
 getEngaged() - when this property is true, the controller will supply power to the motor coils.
 setEngaged(boolean) - this is automatically set to true when calling setTargetPosition

 getRescaleFactor() - get rescaler factor (see bellow)
 setRescaleFactor(float) - applies a factor to the units used by all movement parameters to make the units in your sketch more intuitive.
     For example: if your motor is 400 steps per revolution, after applying a factor of 1/(16*400) - because the basic unit is 1/16th step - setting target
     position to 1.0 will result in exactly one revolution of the motor.
 
 Event functions:

 void positionChange() - called when the stepper's position is changed, based on data rate set with setDataRate.
 void velocityChange() - called when the stepper's velocity is changed, based on data rate set with setDataRate.
 void stopped() - called when the stepper is stopped. Note: there is no feedback to the controller, so it does not know whether the motor shaft is actually moving or not.
   This actually indicates the motor stopped recieving commands from the controller.

 getDataInterval() - returns minimum interval in milliseconds between event triggers
 setDataInterval(int) - allows to set minimum interval in milliseconds between event triggers
 getMinDataInterval() - return minimum possible data interval for opened device
 getMaxDataInterval() - return maximum possible data interval for opened device

 for newer stepper boards (VINT stepper controllers), these functions are also available:
 
 getHoldingCurrentLimit() - this value will activate when the TargetPosition has been reached. It will limit current through the motor.
 setHoldingCurrentLimit(float) - sets the holding current limit to the motor.
 
 enableFailsafe(int) - enable the fail-safe mode with the given fail-safe time.
 getMinFailsafeTime() - returns min fale-safe time
 getMaxFailsafeTime() - returns max fale-safe time
 resetFailsafe() - resets the fail-safe timer. This need to be called periodically to prevent the board to enter fail-safe mode.
 

*/
