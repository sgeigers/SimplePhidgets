package shenkar.SimplePhidgets;

import processing.core.*;
import java.lang.reflect.Method;
import com.phidget22.*;
import java.util.ArrayList;

public class P_Spatial extends Device {
	// events
	Method accelerationChangeEventMethod;  // accelChange
	Method angularRateUpdateEventMethod; // gyroChange
	Method magneticFieldChangeEventMethod; // magnetoChange
	Method spatialChangeEventMethod; // spatialChange
	/*NOT TESTED*/	Method algorithmChangeEventMethod; // algorithmChange
	boolean accelerationChangeFlag = false;
	boolean angularRateUpdateFlag = false;
	boolean magneticFieldChangeFlag = false;
	boolean spatialChangeFlag = false;
	boolean algorithmChangeFlag = false;
	boolean accelerationChangeEventReportChannel = false;
	boolean angularRateUpdateEventReportChannel = false;
	boolean magneticFieldChangeEventReportChannel = false;
	boolean spatialChangeEventReportChannel = false;
	boolean algorithmChangeEventReportChannel = false;

	// real-time events
	Method accelerationChangeEventRTMethod;  // accelChangeRT
	Method angularRateUpdateEventRTMethod; // gyroChangeRT
	Method magneticFieldChangeEventRTMethod; // magnetoChangeRT
	Method spatialChangeEventRTMethod; // spatialChangeRT
	/*NOT TESTED*/	Method algorithmChangeEventRTMethod; // algorithmChangeRT

	// there are 2 types of spatial sensor boards: accelerometer only or 9DOF. the latter need extra 3 objects inside it to get full functionality from precessing
	Spatial spatial;
	Gyroscope gyroscope;
	Magnetometer magnetometer;

	// spatial data that will be updated regularly from events when applicable (depending on connected board):
	double rollAngle;
	double pitchAngle;
	double yawAngle;
	double[] quaternion;
	ArrayList<double[]> compassBearingFilter = new ArrayList<double[]>();
	final int compassBearingFilterSize = 10;

	public P_Spatial(PApplet P5Parent, Channel ChParent, String type, int serialNum, int portNum, int chNum) {
		super(P5Parent, ChParent, type, serialNum, portNum, chNum);

		// pre-opening setup
		try {
			device = new Accelerometer();

			switch (deviceType) {			
			case "1042":  // PhidgetSpatial 3/3/3 Basic
			case "1044":  // PhidgetSpatial Precision 3/3/3 High Resolution
			case "1056":  // PhidgetSpatial 3/3/3
			case "MOT1101":  // Spatial Phidget
				spatial = new Spatial();
				gyroscope = new Gyroscope();
				magnetometer = new Magnetometer();
				break;

			default:
				break;
			}
		}	catch (PhidgetException ex) {
			System.err.println("Could not open device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
		}

		// device opening
		switch (deviceType) {
		case "1008":  // PhidgetAccelerometer 2-Axis
		case "1041":  // PhidgetSpatial 0/0/3 Basic
		case "1043":  // PhidgetSpatial Precision 0/0/3 High Resolution
		case "1049":  // PhidgetSpatial 0/0/3
		case "1053":  // PhidgetAccelerometer 2-Axis
		case "1059":  // PhidgetAccelerometer 3-Axis
			initNoHub();
			break;

		case "MOT1100":  // Accelerometer Phidget
			init(false);
			break;

		case "1042":  // PhidgetSpatial 3/3/3 Basic
		case "1044":  // PhidgetSpatial Precision 3/3/3 High Resolution
		case "1056":  // PhidgetSpatial 3/3/3
			initNoHub();
			try {
				if (serial > -1) {
					spatial.setDeviceSerialNumber(serial);
					gyroscope.setDeviceSerialNumber(serial);
					magnetometer.setDeviceSerialNumber(serial);
				}
				spatial.open(5000);
				gyroscope.open(5000);
				magnetometer.open(5000);
			}
			catch (PhidgetException ex) {
				System.err.println("Could not open device " + deviceType + ". See github.com/sgeigers/SimplePhidgets#reference for help");
			}
			break;

		case "MOT1101":  // Spatial Phidget
			init(false);
			try {
				spatial.setHubPort(hubPort);
				gyroscope.setHubPort(hubPort);
				magnetometer.setHubPort(hubPort);
				spatial.setIsHubPortDevice(false);
				gyroscope.setIsHubPortDevice(false);
				magnetometer.setIsHubPortDevice(false);
				if (serial > -1) {
					spatial.setDeviceSerialNumber(serial);
					gyroscope.setDeviceSerialNumber(serial);
					magnetometer.setDeviceSerialNumber(serial);	
				}
				spatial.open(5000);
				gyroscope.open(5000);
				magnetometer.open(5000);
			}
			catch (PhidgetException ex) {
				System.err.println("Could not open device " + deviceType + ". See github.com/sgeigers/SimplePhidgets#reference for help");
			}
			break;
		}

		// post-opening setup
		try {
			// set maximum data rate as default
			((Accelerometer)device).setDataInterval(((Accelerometer)device).getMinDataInterval());

			switch (deviceType) {			
			case "1042":  // PhidgetSpatial 3/3/3 Basic
			case "1044":  // PhidgetSpatial Precision 3/3/3 High Resolution
			case "1056":  // PhidgetSpatial 3/3/3
			case "MOT1101":  // Spatial Phidget
				spatial.setDataInterval(spatial.getMinDataInterval());
				gyroscope.setDataInterval(gyroscope.getMinDataInterval());
				magnetometer.setDataInterval(magnetometer.getMinDataInterval());
				break;

			default:
				break;
			}

		}	catch (PhidgetException ex) {
			System.err.println("Could not set data interval for device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
		}
		attachListeners();
	}

	// check if "accelChange()", "gyroChange()", "magnetoChange()" or "spatialChange()" or their RT versions were defined in the sketch and create listeners for them.
	void attachListeners() {
		switch (deviceType) {

		// accelerometers only devices have the "accelerationChangeEventRTMethod" always running and updating roll and pitch angles
		case "1008":  // PhidgetAccelerometer 2-Axis
		case "1041":  // PhidgetSpatial 0/0/3 Basic
		case "1043":  // PhidgetSpatial Precision 0/0/3 High Resolution
		case "1049":  // PhidgetSpatial 0/0/3
		case "1053":  // PhidgetAccelerometer 2-Axis
		case "1059":  // PhidgetAccelerometer 3-Axis
		case "MOT1100":  // Accelerometer Phidget

			// accelChange()
			try {
				accelerationChangeEventMethod = PAppletParent.getClass().getMethod("accelChange");
				if (accelerationChangeEventMethod != null) {
					accelerationChangeEventReportChannel = false;
				}
				// we use this inside the real-time event, which is used anyway, to raise the flag.				
			} catch (Exception e) {
				// function "accelChange" not defined
			}

			// accelChange(Channel)
			try {
				accelerationChangeEventMethod = PAppletParent.getClass().getMethod("accelChange", new Class<?>[] { Channel.class });
				if (accelerationChangeEventMethod != null) {
					accelerationChangeEventReportChannel = true;
				}
				// we use this inside the real-time event, which is used anyway, to raise the flag.				
			} catch (Exception e) {
				// function "accelChange" not defined
			}

			// accelChangeRT()
			try {
				accelerationChangeEventRTMethod = PAppletParent.getClass().getMethod("accelChangeRT");
				if (accelerationChangeEventRTMethod != null) {
					if (accelerationChangeEventMethod != null) {
						System.err.println("Cannot use both accelChange() and accelChangeRT()."); 	
						accelerationChangeEventRTMethod =null;
					}
					else {
						accelerationChangeEventReportChannel = false;
					}
				}
			} catch (Exception e) {
				// function "accelChangeRT" not defined
			}

			// accelChangeRT(Channel)
			try {
				accelerationChangeEventRTMethod = PAppletParent.getClass().getMethod("accelChangeRT", new Class<?>[] { Channel.class });
				if (accelerationChangeEventRTMethod != null) {
					if (accelerationChangeEventMethod != null) {
						System.err.println("Cannot use both accelChange() and accelChangeRT()."); 				
						accelerationChangeEventRTMethod =null;
					}
					else {
						accelerationChangeEventReportChannel = true;
					}
				}
			} catch (Exception e) {
				// function "accelChangeRT" not defined
			}

			// we always use this event, for continuously updating pitch  and roll angles
			try {
				((Accelerometer)device).addAccelerationChangeListener(new AccelerometerAccelerationChangeListener() {
					public void onAccelerationChange(AccelerometerAccelerationChangeEvent e) {
						//System.out.println(e.toString());
						calculateTiltRoll(e);
						try {
							if (accelerationChangeEventRTMethod != null) {
								if (accelerationChangeEventReportChannel) {
									accelerationChangeEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
								}
								else {
									accelerationChangeEventRTMethod.invoke(PAppletParent);
								}
							}
						} catch (Exception ex) {
							System.err.println("Disabling accelChangeRT() for " + deviceType + " because of an error:");
							ex.printStackTrace();
							accelerationChangeEventRTMethod = null;
						}
						if (accelerationChangeEventMethod != null) {
							accelerationChangeFlag =true;
						}
					}
				});
			}
			catch (Exception e) {
				System.err.println("Cannot add acceleration change event for device " + deviceType + " because of error: " + e);
			}
			break;

			// if this is a device with other sensors (gyro and magnetometer), accelChange is only set by user. also check for other event methonds
		case "1042":  // PhidgetSpatial 3/3/3 Basic
		case "1044":  // PhidgetSpatial Precision 3/3/3 High Resolution
		case "1056":  // PhidgetSpatial 3/3/3
		case "MOT1101":  // Spatial Phidget
			// accelChange()
			try {
				accelerationChangeEventMethod = PAppletParent.getClass().getMethod("accelChange");
				if (accelerationChangeEventMethod != null) {
					accelerationChangeEventReportChannel = false;
					((Accelerometer)device).addAccelerationChangeListener(new AccelerometerAccelerationChangeListener() {
						public void onAccelerationChange(AccelerometerAccelerationChangeEvent e) {
							//System.out.println(e.toString());
							accelerationChangeFlag = true;
						}
					});
				}
			} catch (Exception e) {
				// function "accelChange" not defined
			}

			// accelChange(Channel)
			try {
				accelerationChangeEventMethod = PAppletParent.getClass().getMethod("accelChange", new Class<?>[] { Channel.class });
				if (accelerationChangeEventMethod != null) {
					accelerationChangeEventReportChannel = true;
					((Accelerometer)device).addAccelerationChangeListener(new AccelerometerAccelerationChangeListener() {
						public void onAccelerationChange(AccelerometerAccelerationChangeEvent e) {
							//System.out.println(e.toString());
							accelerationChangeFlag = true;
						}
					});
				}
			} catch (Exception e) {
				// function "accelChange" not defined
			}

			// accelChangeRT()
			try {
				accelerationChangeEventRTMethod = PAppletParent.getClass().getMethod("accelChangeRT");
				if (accelerationChangeEventRTMethod != null) {
					if (accelerationChangeEventMethod != null) {
						System.err.println("Cannot use both accelChange() and accelChangeRT()."); 				
					}
					else {
						((Accelerometer)device).addAccelerationChangeListener(new AccelerometerAccelerationChangeListener() {
							public void onAccelerationChange(AccelerometerAccelerationChangeEvent e) {
								//System.out.println(e.toString());
								try {
									if (accelerationChangeEventRTMethod != null) {
										accelerationChangeEventRTMethod.invoke(PAppletParent);
									}
								} catch (Exception ex) {
									System.err.println("Disabling accelChangeRT() for " + deviceType + " because of an error:");
									ex.printStackTrace();
									accelerationChangeEventRTMethod = null;
								}
							}
						});
					}
				}
			} catch (Exception e) {
				// function "accelChangeRT" not defined
			}

			// accelChangeRT(Channel)
			try {
				accelerationChangeEventRTMethod = PAppletParent.getClass().getMethod("accelChangeRT", new Class<?>[] { Channel.class });
				if (accelerationChangeEventRTMethod != null) {
					if (accelerationChangeEventMethod != null) {
						System.err.println("Cannot use both accelChange() and accelChangeRT()."); 				
					}
					else {
						((Accelerometer)device).addAccelerationChangeListener(new AccelerometerAccelerationChangeListener() {
							public void onAccelerationChange(AccelerometerAccelerationChangeEvent e) {
								//System.out.println(e.toString());
								try {
									if (accelerationChangeEventRTMethod != null) {
										accelerationChangeEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
									}
								} catch (Exception ex) {
									System.err.println("Disabling accelChangeRT() for " + deviceType + " because of an error:");
									ex.printStackTrace();
									accelerationChangeEventRTMethod = null;
								}
							}
						});
					}
				}
			} catch (Exception e) {
				// function "accelChangeRT" not defined
			}

			// spatialChange()
			try {
				spatialChangeEventMethod = PAppletParent.getClass().getMethod("spatialChange");
				if (spatialChangeEventMethod != null) {
					spatialChangeEventReportChannel = false;
				}
				// we use this inside the real-time event, which is used anyway, to raise the flag.
			} catch (Exception e) {
				// function "accelChange" not defined
			}

			// spatialChange(Channel)
			try {
				spatialChangeEventMethod = PAppletParent.getClass().getMethod("spatialChange", new Class<?>[] { Channel.class });
				if (spatialChangeEventMethod != null) {
					spatialChangeEventReportChannel = true;
				}
				// we use this inside the real-time event, which is used anyway, to raise the flag.
			} catch (Exception e) {
				// function "accelChange" not defined
			}

			// spatialChangeRT()
			try {
				spatialChangeEventRTMethod = PAppletParent.getClass().getMethod("spatialChangeRT");
				if (spatialChangeEventRTMethod != null) {
					if (spatialChangeEventMethod != null) {
						System.err.println("Cannot use both accelChange() and accelChangeRT()."); // for consistancy...
						spatialChangeEventRTMethod = null;
					}
					else {
						spatialChangeEventReportChannel = false;
					}
				}
			} catch (Exception e) {
				// function "spatialChangeRT" not defined
			}

			// spatialChangeRT(Channel)
			try {
				spatialChangeEventRTMethod = PAppletParent.getClass().getMethod("spatialChangeRT", new Class<?>[] { Channel.class });
				if (spatialChangeEventRTMethod != null) {
					if (spatialChangeEventMethod != null) {
						System.err.println("Cannot use both accelChange() and accelChangeRT()."); // for consistancy...
						spatialChangeEventRTMethod = null;
					}
					else {
						spatialChangeEventReportChannel = true;
					}
				}
			} catch (Exception e) {
				// function "spatialChangeRT" not defined
			}

			// we always use this event, for continuously updating pitch, roll and yaw angles
			try {
				spatial.addSpatialDataListener(new SpatialSpatialDataListener() {
					public void onSpatialData(SpatialSpatialDataEvent e) {
						//System.out.println(e.toString());
						calculateSpatialData(e);
						try {
							if (spatialChangeEventRTMethod != null) {
								if (spatialChangeEventReportChannel) {
									spatialChangeEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
								}
								else {
									spatialChangeEventRTMethod.invoke(PAppletParent);
								}
							}
						} catch (Exception ex) {
							System.err.println("Disabling spatialChangeRT(Channel) for " + deviceType + " because of an error:");
							ex.printStackTrace();
							spatialChangeEventRTMethod = null;
						}
						if (spatialChangeEventMethod != null) {
							spatialChangeFlag = true;
						}
					}
				});
			}
			catch (Exception e) {
				System.err.println("Cannot add spatial update event for device " + deviceType + " because of error: " + e);
			}

			// gyroChange()
			try {
				angularRateUpdateEventMethod = PAppletParent.getClass().getMethod("gyroChange");
				if (angularRateUpdateEventMethod != null) {
					angularRateUpdateEventReportChannel = false;
					gyroscope.addAngularRateUpdateListener(new GyroscopeAngularRateUpdateListener() {
						public void onAngularRateUpdate(GyroscopeAngularRateUpdateEvent e) {
							//System.out.println(e.toString());
							angularRateUpdateFlag = true;
						}
					});
				}
			} catch (Exception e) {
				// function "gyroChange" not defined
			}

			// gyroChange(Channel)
			try {
				angularRateUpdateEventMethod = PAppletParent.getClass().getMethod("gyroChange", new Class<?>[] { Channel.class });
				if (angularRateUpdateEventMethod != null) {
					angularRateUpdateEventReportChannel = true;
					gyroscope.addAngularRateUpdateListener(new GyroscopeAngularRateUpdateListener() {
						public void onAngularRateUpdate(GyroscopeAngularRateUpdateEvent e) {
							//System.out.println(e.toString());
							angularRateUpdateFlag = true;
						}
					});
				}
			} catch (Exception e) {
				// function "gyroChange" not defined
			}

			// gyroChangeRT()
			try {
				angularRateUpdateEventRTMethod = PAppletParent.getClass().getMethod("gyroChangeRT");
				if (angularRateUpdateEventRTMethod != null) {
					if (angularRateUpdateEventMethod != null) {
						System.err.println("Cannot use both gyroChange() and gyroChangeRT()."); 				
					}
					else {
						gyroscope.addAngularRateUpdateListener(new GyroscopeAngularRateUpdateListener() {
							public void onAngularRateUpdate(GyroscopeAngularRateUpdateEvent e) {
								//System.out.println(e.toString());
								try {
									if (angularRateUpdateEventRTMethod != null) {
										angularRateUpdateEventRTMethod.invoke(PAppletParent);
									}
								} catch (Exception ex) {
									System.err.println("Disabling gyroChangeRT() for " + deviceType + " because of an error:");
									ex.printStackTrace();
									angularRateUpdateEventRTMethod = null;
								}
							}
						});
					}
				}
			} catch (Exception e) {
				// function "gyroChangeRT()" not defined
			}

			// gyroChangeRT(Channel)
			try {
				angularRateUpdateEventRTMethod = PAppletParent.getClass().getMethod("gyroChangeRT", new Class<?>[] { Channel.class });
				if (angularRateUpdateEventRTMethod != null) {
					if (angularRateUpdateEventMethod != null) {
						System.err.println("Cannot use both gyroChange() and gyroChangeRT()."); 				
					}
					else {
						gyroscope.addAngularRateUpdateListener(new GyroscopeAngularRateUpdateListener() {
							public void onAngularRateUpdate(GyroscopeAngularRateUpdateEvent e) {
								//System.out.println(e.toString());
								try {
									if (angularRateUpdateEventRTMethod != null) {
										angularRateUpdateEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
									}
								} catch (Exception ex) {
									System.err.println("Disabling gyroChangeRT() for " + deviceType + " because of an error:");
									ex.printStackTrace();
									angularRateUpdateEventRTMethod = null;
								}
							}
						});
					}
				}
			} catch (Exception e) {
				// function "gyroChangeRT(Channel)" not defined
			}

			// magnetoChange()
			try {
				magneticFieldChangeEventMethod = PAppletParent.getClass().getMethod("magnetoChange");
				if (magneticFieldChangeEventMethod != null) {
					magneticFieldChangeEventReportChannel = false;
					magnetometer.addMagneticFieldChangeListener(new MagnetometerMagneticFieldChangeListener() {
						public void onMagneticFieldChange(MagnetometerMagneticFieldChangeEvent e) {
							//System.out.println(e.toString());
							magneticFieldChangeFlag = true;
						}
					});
				}
			} catch (Exception e) {
				// function "magnetoChange" not defined
			}

			// magnetoChange(Channel)
			try {
				magneticFieldChangeEventMethod = PAppletParent.getClass().getMethod("magnetoChange", new Class<?>[] { Channel.class });
				if (magneticFieldChangeEventMethod != null) {
					magneticFieldChangeEventReportChannel = true;
					magnetometer.addMagneticFieldChangeListener(new MagnetometerMagneticFieldChangeListener() {
						public void onMagneticFieldChange(MagnetometerMagneticFieldChangeEvent e) {
							//System.out.println(e.toString());
							magneticFieldChangeFlag = true;
						}
					});
				}
			} catch (Exception e) {
				// function "magnetoChange(Channel)" not defined
			}

			// magnetoChangeRT()
			try {
				magneticFieldChangeEventRTMethod = PAppletParent.getClass().getMethod("magnetoChangeRT");
				if (magneticFieldChangeEventRTMethod != null) {
					if (magneticFieldChangeEventMethod != null) {
						System.err.println("Cannot use both magnetoChange() and magnetoChangeRT()."); 				
					}
					else {
						magnetometer.addMagneticFieldChangeListener(new MagnetometerMagneticFieldChangeListener() {
							public void onMagneticFieldChange(MagnetometerMagneticFieldChangeEvent e) {
								//System.out.println(e.toString());
								try {
									if (magneticFieldChangeEventRTMethod != null) {
										magneticFieldChangeEventRTMethod.invoke(PAppletParent);
									}
								} catch (Exception ex) {
									System.err.println("Disabling magnetoChangeRT() for " + deviceType + " because of an error:");
									ex.printStackTrace();
									magneticFieldChangeEventRTMethod = null;
								}
							}
						});
					}
				}
			} catch (Exception e) {
				// function "magnetoChangeRT(Channel)" not defined
			}

			// magnetoChangeRT(Channel)
			try {
				magneticFieldChangeEventRTMethod = PAppletParent.getClass().getMethod("magnetoChangeRT", new Class<?>[] { Channel.class });
				if (magneticFieldChangeEventRTMethod != null) {
					if (magneticFieldChangeEventMethod != null) {
						System.err.println("Cannot use both magnetoChange() and magnetoChangeRT()."); 				
					}
					else {
						magnetometer.addMagneticFieldChangeListener(new MagnetometerMagneticFieldChangeListener() {
							public void onMagneticFieldChange(MagnetometerMagneticFieldChangeEvent e) {
								//System.out.println(e.toString());
								try {
									if (magneticFieldChangeEventRTMethod != null) {
										magneticFieldChangeEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
									}
								} catch (Exception ex) {
									System.err.println("Disabling magnetoChangeRT() for " + deviceType + " because of an error:");
									ex.printStackTrace();
									magneticFieldChangeEventRTMethod = null;
								}
							}
						});
					}
				}
			} catch (Exception e) {
				// function "magnetoChangeRT" not defined
			}
			break;

		default:
			break;
		}


		try {
			if (spatial.getChannelSubclass() == ChannelSubclass.SPATIAL_AHRS) {
				// algorithmChange()
				try {
					algorithmChangeEventMethod = PAppletParent.getClass().getMethod("algorithmChange");
					// we use this inside the real-time event, which is used anyway, to raise the flag.
					if (algorithmChangeEventMethod != null) {
						algorithmChangeEventReportChannel = false;
					}
				}
				catch (Exception e) {
					// function "algorithmChange" not defined
				}

				// algorithmChange(Channel)
				try {
					algorithmChangeEventMethod = PAppletParent.getClass().getMethod("algorithmChange");
					// we use this inside the real-time event, which is used anyway, to raise the flag.
					if (algorithmChangeEventMethod != null) {
						algorithmChangeEventReportChannel = true;
					}
				} catch (Exception e) {
					// function "algorithmChange(Channel)" not defined
				}

				// algorithmChangeRT()
				try {
					algorithmChangeEventRTMethod = PAppletParent.getClass().getMethod("algorithmChangeRT");
					if (algorithmChangeEventRTMethod != null) {
						if (algorithmChangeEventMethod != null) {
							System.err.println("Cannot use both algorithmChange() andalgorithmChangeRT()."); // for consistancy...
							algorithmChangeEventRTMethod = null;
						}
						else {
							algorithmChangeEventReportChannel = false;
						}
					}
				} catch (Exception e) {
					// function "algorithmChange" not defined
				}

				// algorithmChangeRT(Channel)
				try {
					algorithmChangeEventRTMethod = PAppletParent.getClass().getMethod("algorithmChangeRT", new Class<?>[] { Channel.class });
					if (algorithmChangeEventRTMethod != null) {
						if (algorithmChangeEventMethod != null) {
							System.err.println("Cannot use both algorithmChange() andalgorithmChangeRT()."); // for consistancy...
							algorithmChangeEventRTMethod = null;
						}
						else {
							algorithmChangeEventReportChannel = true;
						}
					}
				} catch (Exception e) {
					// function "algorithmChange" not defined
				}

				// we always use this event, for continuously updating quaternion
				try {
					spatial.addAlgorithmDataListener(new SpatialAlgorithmDataListener() {
						public void onAlgorithmData(SpatialAlgorithmDataEvent e) {
							//System.out.println(e.toString());
							quaternion = (e.getQuaternion()).clone();
							try {
								if (algorithmChangeEventRTMethod != null) {
									if (algorithmChangeEventReportChannel) {
										algorithmChangeEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
									}
									else {
										algorithmChangeEventRTMethod.invoke(PAppletParent);
									}
								}
							} catch (Exception ex) {
								System.err.println("Disabling algorithmChangeRT(Channel) for " + deviceType + " because of an error:");
								ex.printStackTrace();
								algorithmChangeEventRTMethod = null;
							}
							if (algorithmChangeEventRTMethod != null) {
								algorithmChangeFlag = true;
							}
						}
					});
				}
				catch (Exception e) {
					System.err.println("Cannot add algorithm update event for device " + deviceType + " because of error: " + e);
				}
			}
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get channel sub class for device " + deviceType + " because of error: " + ex);
		}
	}

	//This finds a magnetic north bearing, correcting for board tilt and roll as measured by the accelerometer
	//This doesn't account for dynamic acceleration - ie accelerations other than gravity will throw off the calculation
	//Calculations based on AN4248 and taken from Phidgets website
	double[] lastAngles = { 0, 0, 0 };
	void calculateSpatialData(SpatialSpatialDataEvent e) {
		double[] gravity = e.getAcceleration();
		double[] magField = e.getMagneticField();

		//Roll Angle - about axis 0
		//  tan(roll angle) = gy/gz
		//  Use Atan2 so we have an output os (-180 - 180) degrees
		rollAngle = Math.atan2(gravity[1], gravity[2]);

		//Pitch Angle - about axis 1
		//  tan(pitch angle) = -gx / (gy * sin(roll angle) * gz * cos(roll angle))
		//  Pitch angle range is (-90 - 90) degrees
		pitchAngle = Math.atan(-gravity[0] / (gravity[1] * Math.sin(rollAngle) + gravity[2] * Math.cos(rollAngle)));

		//Yaw Angle - about axis 2
		//  tan(yaw angle) = (mz * sin(roll) - my * cos(roll)) /
		//                   (mx * cos(pitch) + my * sin(pitch) * sin(roll) + mz * sin(pitch) * cos(roll))
		//  Use Atan2 to get our range in (-180 - 180)
		//
		//  Yaw angle == 0 degrees when axis 0 is pointing at magnetic north
		yawAngle = Math.atan2(magField[2] * Math.sin(rollAngle) - magField[1] * Math.cos(rollAngle),
				magField[0] * Math.cos(pitchAngle) + magField[1] * Math.sin(pitchAngle) * Math.sin(rollAngle) + magField[2] * Math.sin(pitchAngle) * Math.cos(rollAngle));

		double[] angles = { rollAngle, pitchAngle, yawAngle };

		//we low-pass filter the angle data so that it looks nicer on-screen
		//make sure the filter buffer doesn't have values passing the -180<->180 mark
		//Only for Roll and Yaw - Pitch will never have a sudden switch like that
		for (int i = 0; i < 3; i += 2) {
			if (Math.abs(angles[i] - lastAngles[i]) > 3)
				for (double[] value : compassBearingFilter)
					if (angles[i] > lastAngles[i])
						value[i] += 2 * Math.PI;
					else
						value[i] -= 2 * Math.PI;
		}

		lastAngles = angles.clone();

		compassBearingFilter.add(angles.clone());
		if (compassBearingFilter.size() > compassBearingFilterSize)
			compassBearingFilter.remove(0);

		yawAngle = pitchAngle = rollAngle = 0;
		for (double[] record : compassBearingFilter) {
			rollAngle += record[0];
			pitchAngle += record[1];
			yawAngle += record[2];
		}
		yawAngle /= compassBearingFilter.size();
		pitchAngle /= compassBearingFilter.size();
		rollAngle /= compassBearingFilter.size();

		//Convert radians to degrees
		yawAngle = Math.toDegrees(yawAngle);
		pitchAngle = Math.toDegrees(pitchAngle);
		rollAngle = Math.toDegrees(rollAngle);
	}		

	//This finds tilt and roll as measured by the accelerometer
	//This doesn't account for dynamic acceleration - ie accelerations other than gravity will throw off the calculation
	//Calculations based on AN4248 and taken from Phidgets website
	void calculateTiltRoll(AccelerometerAccelerationChangeEvent e) {
		double[] gravity = e.getAcceleration();

		//Roll Angle - about axis 0
		//  tan(roll angle) = gy/gz
		//  Use Atan2 so we have an output os (-180 - 180) degrees
		rollAngle = Math.atan2(gravity[1], gravity[2]);

		//Pitch Angle - about axis 1
		//  tan(pitch angle) = -gx / (gy * sin(roll angle) * gz * cos(roll angle))
		//  Pitch angle range is (-90 - 90) degrees
		pitchAngle = Math.atan(-gravity[0] / (gravity[1] * Math.sin(rollAngle) + gravity[2] * Math.cos(rollAngle)));

		double[] angles = { rollAngle, pitchAngle, 0 };

		//we low-pass filter the angle data so that it looks nicer on-screen
		//make sure the filter buffer doesn't have values passing the -180<->180 mark
		//Only for Roll - Pitch will never have a sudden switch like that
		if (Math.abs(angles[0] - lastAngles[0]) > 3)
			for (double[] value : compassBearingFilter)
				if (angles[0] > lastAngles[0])
					value[0] += 2 * Math.PI;
				else
					value[0] -= 2 * Math.PI;

		lastAngles = angles.clone();

		compassBearingFilter.add(angles.clone());
		if (compassBearingFilter.size() > compassBearingFilterSize)
			compassBearingFilter.remove(0);

		pitchAngle = rollAngle = 0;
		for (double[] record : compassBearingFilter) {
			rollAngle += record[0];
			pitchAngle += record[1];
		}
		pitchAngle /= compassBearingFilter.size();
		rollAngle /= compassBearingFilter.size();

		//Convert radians to degrees
		pitchAngle = Math.toDegrees(pitchAngle);
		rollAngle = Math.toDegrees(rollAngle);
	}		

	@Override
	public void draw() {
		if (accelerationChangeFlag) {
			accelerationChangeFlag = false;
			try {
				if (accelerationChangeEventMethod != null) {
					if (accelerationChangeEventReportChannel) {
						accelerationChangeEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						accelerationChangeEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling accelChange() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				accelerationChangeEventMethod = null;
			}
		}

		if (spatialChangeFlag) {
			spatialChangeFlag = false;
			try {
				if (spatialChangeEventMethod != null) {
					if (spatialChangeEventReportChannel) {
						spatialChangeEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						spatialChangeEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling spatialChange() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				spatialChangeEventMethod = null;
			}
		}

		if (angularRateUpdateFlag) {
			angularRateUpdateFlag = false;
			try {
				if (angularRateUpdateEventMethod != null) {
					if (angularRateUpdateEventReportChannel) {
						angularRateUpdateEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						angularRateUpdateEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling gyroChange() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				angularRateUpdateEventMethod = null;
			}
		}

		if (magneticFieldChangeFlag) {
			magneticFieldChangeFlag = false;
			try {
				if (magneticFieldChangeEventMethod != null) {
					if (magneticFieldChangeEventReportChannel) {
						magneticFieldChangeEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						magneticFieldChangeEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling magnetoChange() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				magneticFieldChangeEventMethod = null;
			}
		}

		if (algorithmChangeFlag) {
			algorithmChangeFlag = false;
			try {
				if (algorithmChangeEventMethod != null) {
					if (algorithmChangeEventReportChannel ) {
						algorithmChangeEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						algorithmChangeEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling algorithmChange() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				algorithmChangeEventMethod = null;
			}
		}
	}

	@Override
	public float getRoll() {
		return (float)rollAngle;
	}

	@Override
	public float getPitch() {
		return (float)pitchAngle;
	}

	@Override
	public float getYaw() {
		if ((deviceType=="1042") || (deviceType=="1044") || (deviceType=="1056") || (deviceType=="MOT1101")) { 
			return (float)yawAngle;
		}
		else {
			System.err.println("getYaw() is not valid for device of type " + deviceType);	
		}
		return 0.0f;
	}

	@Override
	public float[] getQuaternion() {
		try {
			if (spatial.getChannelSubclass() == ChannelSubclass.SPATIAL_AHRS) { 
				if (quaternion != null) {
					float[] quaf = new float[4];
					for (int i=0; i<4; i++)
						quaf[i] = (float)quaternion[i];
					return quaf;
				}
				else {
					System.err.println("quaternion not defined");
				}
			}
			else {
				System.err.println("getQuaternion() is not valid for device of type " + deviceType);	
			}
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get quaternion from device " + deviceType + " because of error: " + ex);
		}
		return new float[4];		
	}

	@Override
	public float getAcceleration() {
		System.out.println("For spatial devices, use \"getAccelerationArray\" instead of \"getAcceleration\"");
		return 0;
	}

	@Override
	public float getMinAcceleration() {
		System.out.println("For spatial devices, use \"getMinAccelerationArray\" instead of \"getMinAcceleration\"");
		return 0;
	}

	@Override
	public float getMaxAcceleration() {
		System.out.println("For spatial devices, use \"getMaxAccelerationArray\" instead of \"getMaxAcceleration\"");
		return 0;
	}

	@Override
	public float[] getAccelerationArray() {
		try {
			double[] accd = ((Accelerometer)device).getAcceleration();
			float[] accf = new float[3];
			for (int i=0; i<((Accelerometer)device).getAxisCount(); i++)
				accf[i] = (float)accd[i];
			return accf;
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get acceleration values from device " + deviceType + " because of error: " + ex);
		}
		return new float[3];		
	}

	@Override
	public float[] getMinAccelerationArray() {
		try {
			double[] accd = ((Accelerometer)device).getMinAcceleration();
			float[] accf = new float[3];
			for (int i=0; i<((Accelerometer)device).getAxisCount(); i++)
				accf[i] = (float)accd[i];
			return accf;
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min acceleration values from device " + deviceType + " because of error: " + ex);
		}
		return new float[3];		
	}

	@Override
	public float[] getMaxAccelerationArray() {
		try {
			double[] accd = ((Accelerometer)device).getMaxAcceleration();
			float[] accf = new float[3];
			for (int i=0; i<((Accelerometer)device).getAxisCount(); i++)
				accf[i] = (float)accd[i];
			return accf;
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get maxacceleration values from device " + deviceType + " because of error: " + ex);
		}
		return new float[3];		
	}

	@Override
	public float[] getAngularRate() {
		if ((deviceType=="1042") || (deviceType=="1044") || (deviceType=="1056") || (deviceType=="MOT1101")) { 
			try {
				double[] anrd = gyroscope.getAngularRate();
				float[] anrf = new float[3];
				for (int i=0; i<3; i++)
					anrf[i] = (float)anrd[i];
				return anrf;
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get angular rate values from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getAngularRate() is not valid for device of type " + deviceType);	
		}
		return new float[3];		
	}

	@Override
	public float[] getMinAngularRate() {
		if ((deviceType=="1042") || (deviceType=="1044") || (deviceType=="1056") || (deviceType=="MOT1101")) { 
			try {
				double[] anrd = gyroscope.getMinAngularRate();
				float[] anrf = new float[3];
				for (int i=0; i<3; i++)
					anrf[i] = (float)anrd[i];
				return anrf;
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get min angular rate values from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getMinAngularRate() is not valid for device of type " + deviceType);	
		}
		return new float[3];		
	}

	@Override
	public float[] getMaxAngularRate() {
		if ((deviceType=="1042") || (deviceType=="1044") || (deviceType=="1056") || (deviceType=="MOT1101")) { 
			try {
				double[] anrd = gyroscope.getMaxAngularRate();
				float[] anrf = new float[3];
				for (int i=0; i<3; i++)
					anrf[i] = (float)anrd[i];
				return anrf;
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get max angular rate values from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getMaxAngularRate() is not valid for device of type " + deviceType);	
		}
		return new float[3];		
	}

	@Override
	public float[] getMagneticField() {
		if ((deviceType=="1042") || (deviceType=="1044") || (deviceType=="1056") || (deviceType=="MOT1101")) { 
			try {
				double[] magd = magnetometer.getMagneticField();
				float[] magf = new float[3];
				for (int i=0; i<3; i++)
					magf[i] = (float)magd[i];
				return magf;
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get magnetic field values from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getMagneticField() is not valid for device of type " + deviceType);	
		}
		return new float[3];		
	}

	@Override
	public float[] getMinMagneticField() {
		if ((deviceType=="1042") || (deviceType=="1044") || (deviceType=="1056") || (deviceType=="MOT1101")) { 
			try {
				double[] magd = magnetometer.getMinMagneticField();
				float[] magf = new float[3];
				for (int i=0; i<3; i++)
					magf[i] = (float)magd[i];
				return magf;
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get min magnetic field values from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getMinMagneticField() is not valid for device of type " + deviceType);	
		}
		return new float[3];		
	}

	@Override
	public float[] getMaxMagneticField() {
		if ((deviceType=="1042") || (deviceType=="1044") || (deviceType=="1056") || (deviceType=="MOT1101")) { 
			try {
				double[] magd = magnetometer.getMaxMagneticField();
				float[] magf = new float[3];
				for (int i=0; i<3; i++)
					magf[i] = (float)magd[i];
				return magf;
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get max magnetic field values from device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getMaxMagneticField() is not valid for device of type " + deviceType);	
		}
		return new float[3];		
	}

	@Override
	public int getDataInterval() {
		try {
			// although each class might have its own data interval, it seems they all have same limitations for all devices, so we can check only accelerometer
			return ((Accelerometer)device).getDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public void setDataInterval(int dataInterval) {
		try {
			((Accelerometer)device).setDataInterval(dataInterval);

			switch (deviceType) {			
			case "1042":  // PhidgetSpatial 3/3/3 Basic
			case "1044":  // PhidgetSpatial Precision 3/3/3 High Resolution
			case "1056":  // PhidgetSpatial 3/3/3
			case "MOT1101":  // Spatial Phidget
				spatial.setDataInterval(dataInterval);
				gyroscope.setDataInterval(dataInterval);
				magnetometer.setDataInterval(dataInterval);
				break;

			default:
				break;
			}
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set data interval value to device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public int getMinDataInterval() {
		try {
			// although each class might have its own data interval, it seems they all have same limitations for all devices, so we can check only accelerometer
			return ((Accelerometer)device).getMinDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public int getMaxDataInterval() {
		try {
			// although each class might have its own data interval, it seems they all have same limitations for all devices, so we can check only accelerometer
			return ((Accelerometer)device).getMaxDataInterval();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max data interval value from device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	@Override
	public float getAccelerationChangeTrigger() {
		try {
			return (float)(((Accelerometer)device).getAccelerationChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get acceleration change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public void setAccelerationChangeTrigger(float accelerationChangeTrigger) {
		try {
			((Accelerometer)device).setAccelerationChangeTrigger((double)accelerationChangeTrigger);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set acceleration change trigger for device " + deviceType + " because of error: " + ex);
		}
	}

	@Override
	public float getMinAccelerationChangeTrigger() {
		try {
			return (float)(((Accelerometer)device).getMinAccelerationChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get min acceleration change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public float getMaxAccelerationChangeTrigger() {
		try {
			return (float)(((Accelerometer)device).getMaxAccelerationChangeTrigger());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get max acceleration change trigger for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public float getMagneticFieldChangeTrigger() {
		if ((deviceType=="1042") || (deviceType=="1044") || (deviceType=="1056") || (deviceType=="MOT1101")) { 
			try {
				return (float)(magnetometer.getMagneticFieldChangeTrigger());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get magnetic field change trigger for device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getMagneticFieldChangeTrigger() is not valid for device of type " + deviceType);	
		}
		return 0.0f;
	}

	@Override
	public void setMagneticFieldChangeTrigger(float magneticFieldChangeTrigger) {
		if ((deviceType=="1042") || (deviceType=="1044") || (deviceType=="1056") || (deviceType=="MOT1101")) { 
			try {
				magnetometer.setMagneticFieldChangeTrigger((double)magneticFieldChangeTrigger);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set magnetic field change trigger for device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("setMagneticFieldChangeTrigger(float) is not valid for device of type " + deviceType);	
		}
	}

	@Override
	public float getMinMagneticFieldChangeTrigger() {
		if ((deviceType=="1042") || (deviceType=="1044") || (deviceType=="1056") || (deviceType=="MOT1101")) { 
			try {
				return (float)(magnetometer.getMinMagneticFieldChangeTrigger());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get min magnetic field change trigger for device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getMinMagneticFieldChangeTrigger() is not valid for device of type " + deviceType);	
		}
		return 0.0f;
	}

	@Override
	public float getMaxMagneticFieldChangeTrigger() {
		if ((deviceType=="1042") || (deviceType=="1044") || (deviceType=="1056") || (deviceType=="MOT1101")) { 
			try {
				return (float)(magnetometer.getMaxMagneticFieldChangeTrigger());
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get max magnetic field change trigger for device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getMaxMagneticFieldChangeTrigger() is not valid for device of type " + deviceType);	
		}
		return 0.0f;
	}

	@Override
	public int getAxisCount() {
		try {
			int c = ((Accelerometer)device).getAxisCount();
			if ((deviceType=="1042") || (deviceType=="1044") || (deviceType=="1056") || (deviceType=="MOT1101")) {
				c += gyroscope.getAxisCount();
				c += magnetometer.getAxisCount();
			}
			return c;
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get axis count for device " + deviceType + " because of error: " + ex);
		}
		return 0;
	}

	// timestamp is taken from the accelerometer. This may be problematic in specific cases, but seems rare enough to leave as is for now
	@Override
	public float getTimestamp() {
		try {
			return (float)(((Accelerometer)device).getTimestamp());
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get timestamp for device " + deviceType + " because of error: " + ex);
		}
		return 0.0f;
	}

	@Override
	public void zeroGyro() {
		if ((deviceType=="1042") || (deviceType=="1044") || (deviceType=="1056") || (deviceType=="MOT1101")) { 
			try {
				gyroscope.zero();
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot zero gyro " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("zeroGyro() is not valid for device of type " + deviceType);	
		}
	}

	@Override
	public void setMagnetometerCorrectionParameters(
			float magneticField,
			float offset0,
			float offset1,
			float offset2,
			float gain0,
			float gain1,
			float gain2,
			float T0,
			float T1,
			float T2,
			float T3,
			float T4,
			float T5) {
		if ((deviceType=="1042") || (deviceType=="1044") || (deviceType=="1056") || (deviceType=="MOT1101")) { 
			try {
				magnetometer.setCorrectionParameters(
						(double) magneticField,
						(double) offset0,
						(double) offset1,
						(double) offset2,
						(double) gain0,
						(double) gain1,
						(double) gain2,
						(double) T0,
						(double) T1,
						(double) T2,
						(double) T3,
						(double) T4,
						(double) T5);
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot set magnetometercorrection parameters for device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("setMagnetometerCorrectionParameters(float, float, float , ...) is not valid for device of type " + deviceType);	
		}
	}

	@Override
	public void resetMagnetometerCorrectionParameters() {
		if ((deviceType=="1042") || (deviceType=="1044") || (deviceType=="1056") || (deviceType=="MOT1101")) { 
			try {
				magnetometer.resetCorrectionParameters();
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot reset magnetometercorrection parameters for device" + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("resetMagnetometerCorrectionParameters() is not valid for device of type " + deviceType);	
		}
	}

	@Override
	public void saveMagnetometerCorrectionParameters() {
		if ((deviceType=="1042") || (deviceType=="1044") || (deviceType=="1056") || (deviceType=="MOT1101")) { 
			try {
				magnetometer.saveCorrectionParameters();
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot save magnetometer correction parameters for device" + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("saveMagnetometerCorrectionParameters() is not valid for device of type " + deviceType);	
		}
	}

	@Override
	public String getAlgorithm() {  // NONE, IMU or AHRS   -   this seems to do nothing currently...
		if ((deviceType=="1042") || (deviceType=="1044") || (deviceType=="1056") || (deviceType=="MOT1101")) { 
			try {
				SpatialAlgorithm alg = spatial.getAlgorithm();
				switch (alg) {
				case NONE:
					return "NONE";
				case AHRS:
					return "AHRS";
				case IMU:
					return "IMU";
				}
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get algorithm for device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("getAlgorithm() is not valid for device of type " + deviceType);	
		}
		return "";
	}

	@Override
	public void setAlgorithm(String alg) {  // NONE, IMU or AHRS   -   this seems to do nothing currently...
		if ((deviceType=="1042") || (deviceType=="1044") || (deviceType=="1056")) { 
			try {
				switch (alg) {
				case "NONE":
					spatial.setAlgorithm(SpatialAlgorithm.NONE);
					break;
				case "AHRS":
					spatial.setAlgorithm(SpatialAlgorithm.AHRS);
					break;
				case "IMU":
					spatial.setAlgorithm(SpatialAlgorithm.IMU);
					break;
				default:
					System.err.println("Algorithm \"" + alg + "\" not applicable for device " + deviceType + ". use only \"NONE\", \"AHRS\" or \"IMU\".");
					break;
				}
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot get algorithm for device " + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("setAlgorithm() is not valid for device of type " + deviceType);	
		}
	}

	@Override
	public void zeroAlgorithm() {
		if ((deviceType=="1042") || (deviceType=="1044") || (deviceType=="1056")) { 
			try {
				spatial.zeroAlgorithm();
			}
			catch (PhidgetException ex) {
				System.err.println("Cannot reset algorithm for device" + deviceType + " because of error: " + ex);
			}
		}
		else {
			System.err.println("zeroAlgorithm() is not valid for device of type " + deviceType);	
		}
	}	
}
