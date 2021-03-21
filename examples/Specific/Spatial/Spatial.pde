/*
 This example demonstrates a simple use of 9DOF spatial phidget (MOT1101)
 It should work as is with any 9 degrees-of-freedom device from Phidgets.
 The use of accelerometer-only devices is more limited - you can only get roll and pitch angles (no yaw angle)
*/

import shenkar.SimplePhidgets.*;

Channel mySpatial;

void setup() {
  size(400, 400, P3D);
  mySpatial = new Channel(this, "MOT1101");
}

void draw() {
  float pitch = mySpatial.getPitch(); // get pitch angle in gedrees
  float roll = mySpatial.getRoll(); // get roll angle in gedrees
  float yaw = mySpatial.getYaw(); // get yaw angle in gedrees
  
  // drawing a box representing the MOT1101 and rotating it in all 3 axes to represent its spatial orientation
  background(0);
  translate(width/2, height/2);
  rotateZ(radians(yaw));
  rotateX(radians(roll));
  rotateY(radians(pitch));
  box(120,150,60);
  translate(0,80, 0);
  box(50, 30, 10);
}

/*
 All functions for spatial channel:
 
 getRoll() - returns roll angle (rotation around x axis) in degrees (float)
 getPitch() - returns pitch angle (rotation around y axis) in degrees (float)
 getYaw() - returns yaw angle (rotation around z axis) in degrees (float)
 
 Advanced functions:
 
 getQuaternion() - [only valid for 1044_1 phidget] returns a quaternion representing current rotation (4 floats array)
 getAccelerationArray() - returns raw acceleration values in g (earth's gravity) (3 floats array)
 getMinAccelerationArray() - return minimum values of acceleration (3 float array)
 getMaxAccelerationArray() - return maximum values of acceleration (3 float array)
 getAngularRate() - returns raw angular rate values in deg/sec (3 float array)
 getMinAngularRate - returns minimum values of angular rate (3 float array)
 getMaxAngularRate - returns maximum values of angular rate (3 float array)
 getMagneticField - returns magnetic field in gauss (3 float array)
 getMinMagneticField - returns minimum values of magnetic field (3 float array)
 getMaxMagneticField - returns maximum values of magnetic field (3 float array)
 zeroGyro() - Re-zeros the gyroscope in 1-2 seconds. See your device's User Guide for more information on dealing with drift.
    - The device must be stationary when zeroing.
    - The angular rate will be reported as 0.0°/s while zeroing.
 setMagnetometerCorrectionParameters(float, float, ...) - calibrate the magnetometer for the environment it will be used in. See your device's User Guide for more information.
 resetMagnetometerCorrectionParameters() - resets the magnetometer calibration parameters
 saveMagnetometerCorrectionParameters() - saves the calibration parameters
 getAlgorithm() - for the 1044_1, returns the used algorithm for calculating orientatyion quaternion (NONE, AHRS or IMU) (String)
 setAlgorithm(String) - for the 1044_1, set the algorithm to use for calculating orientatyion quaternion (NONE, AHRS or IMU)
 zeroAlgorithm() - for the 1044_1, zeros the AHRS algorithm
 
 Event functions:

 void accelChange() - called when the channel detects a change in acceleration
 void gyroChange() - called when the channel detects a change in angular rate
 void magnetoChange() - called when the channel detects a change in magnetic field
 void spatialChange() - called when the channel detects a change in any of its sensors (accelerometer, gyroscope, etc.)
 void algorithmChange() - called when the algorithm calculates new data (quaternion). Currently only valid for 1044_1 device. see User Guide -> 3.1 on https://www.phidgets.com/?prodid=1158

 void accelChangeRT() - real-time version of the event. See https://github.com/sgeigers/Phidgets4Processing
 void gyroChangeRT() - real-time version of the event.
 void magnetoChangeRT() - real-time version of the event.
 void spatialChangeRT() - real-time version of the event.
 void algorithmChangeRT() - real-time version of the event.

 getTimestamp() - returns the most recent timestamp value that the channel has reported (last measurement) in milliseconds (float)

 getDataInterval() - returns minimum interval in milliseconds between event triggers
 setDataInterval(int) - allows to set minimum interval in milliseconds between event triggers
 getMinDataInterval() - return minimum possible data interval for opened device
 getMaxDataInterval() - return maximum possible data interval for opened device
 
 getAccelerationChangeTrigger() - returns minimum change for triggering the accelChange event (g)
 setAccelerationChangeTrigger(float) - the event is triggered only when at least the selected change is detected by the accelerometer (g)
 getMinAccelerationChangeTrigger() - The minimum value that AccelerationChangeTrigger can be set to.
 getMaxAccelerationChangeTrigger() - The maximum value that AccelerationChangeTrigger can be set to.

 getMagneticFieldChangeTrigger() - returns minimum change for triggering the magnetoChange event (gauss)
 setMagneticFieldChangeTrigger(float) - the event is triggered only when at least the selected change is detected by the magnetometer (gauss)
 getMinMagneticFieldChangeTrigger() - The minimum value that MagneticFieldChangeTrigger can be set to.
 getMaxMagneticFieldChangeTrigger() - The maximum value that MagneticFieldChangeTrigger can be set to.

*/
