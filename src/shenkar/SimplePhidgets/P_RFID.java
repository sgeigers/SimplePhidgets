package shenkar.SimplePhidgets;

import processing.core.*;
import java.lang.reflect.Method;
import com.phidget22.*;

public class P_RFID extends Device {
	// events
	Method tagEventMethod;  // tag
	Method tagLostEventMethod; // tagLost
	boolean tagFlag = false;
	boolean tagLostFlag = false;
	boolean tagEventReportChannel = false;
	boolean tagLostEventReportChannel = false;

	// real-time events
	Method tagEventRTMethod;  // capacitiveTouched
	Method tagLostEventRTMethod; // capacitiveReleased
	boolean RTTagEventRegister = false;
	boolean RTTagLostEventRegister = false;
	
	public P_RFID(PApplet P5Parent, Channel ChParent, String type, int serialNum, int portNum, int chNum) {
		super(P5Parent, ChParent, type, serialNum, portNum, chNum);

		// pre-opening setup
		try {
			device = new RFID();
		}	catch (PhidgetException ex) {
			System.err.println("Could not open device " + deviceType + " on port " + portNum + ". See help on github.com/sgeigers/SimplePhidgets#reference");
			PAppletParent.exit();
		}
				
		// device opening
		initNoHub();

		// post-opening setup
		attachListeners();
	}

	// check if "tag()" or "tagLost()" were defined in the sketch and create listeners for them.
	void attachListeners() {
		
		// tag()
		try {
			tagEventMethod =  PAppletParent.getClass().getMethod("tag");
			if (tagEventMethod != null) {
				tagEventReportChannel = false;
				((RFID)device).addTagListener(new RFIDTagListener() {
					public void onTag(RFIDTagEvent  e) {
						//System.out.println(e.toString());
						tagFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "tag" not defined
		}

		// tag(Channel)
		try {
			tagEventMethod =  PAppletParent.getClass().getMethod("tag", new Class<?>[] { Channel.class });
			if (tagEventMethod != null) {
				tagEventReportChannel = true;
				((RFID)device).addTagListener(new RFIDTagListener() {
					public void onTag(RFIDTagEvent e) {
						//System.out.println(e.toString());
						tagFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "tag(Channel)" not defined
		}

		// tagRT()
		try {
			tagEventRTMethod =  PAppletParent.getClass().getMethod("tagRT");
			if (tagEventRTMethod != null) {
				if (tagEventMethod != null) {
					System.err.println("Cannot use both tag() and tagRT()."); 
				}
				else {			
					RTTagEventRegister = true;
					tagEventReportChannel = false;
				}
			}
		} catch (Exception e) {
			// function "tagdRT" not defined
		}

		// tagRT(Channel)
		try {
			tagEventRTMethod =  PAppletParent.getClass().getMethod("tagRT", new Class<?>[] { Channel.class });
			if (tagEventRTMethod != null) {
				if (tagEventMethod != null) {
					System.err.println("Cannot use both tag() and tagRT()."); 
				}
				else {			
					RTTagEventRegister = true;
					tagEventReportChannel = true;
				}
			}
		} catch (Exception e) {
			// function "tagdRT(channel)" not defined
		}

		// tagLost()
		try {
			tagLostEventMethod =  PAppletParent.getClass().getMethod("tagLost");
			if (tagLostEventMethod != null) {
				tagLostEventReportChannel = false;
				((RFID)device).addTagLostListener(new RFIDTagLostListener() {
					public void onTagLost(RFIDTagLostEvent e) {
						//System.out.println(e.toString());
						tagLostFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "tagdLost" not defined
		}

		// tagLost(Channel)
		try {
			tagLostEventMethod =  PAppletParent.getClass().getMethod("tagLost", new Class<?>[] { Channel.class });
			if (tagLostEventMethod != null) {
				tagLostEventReportChannel = true;
				((RFID)device).addTagLostListener(new RFIDTagLostListener() {
					public void onTagLost(RFIDTagLostEvent e) {
						//System.out.println(e.toString());
						tagLostFlag = true;
					}
				});
			}
		} catch (Exception e) {
			// function "tagdLost(channel)" not defined
		}

		// capacitiveReleasedRT()
		try {
			tagLostEventRTMethod =  PAppletParent.getClass().getMethod("tagLostRT");
			if (tagLostEventRTMethod != null) {
				if (tagLostEventMethod != null) {
					System.err.println("Cannot use both tagLost() and tagLostRT()."); 
				}
				else {			
					RTTagLostEventRegister = true;
					tagLostEventReportChannel = false;
				}
			}
		} catch (Exception e) {
			// function "tagdLostRT" not defined
		}

		// capacitiveReleasedRT(Channel)
		try {
			tagLostEventRTMethod =  PAppletParent.getClass().getMethod("tagLostRT", new Class<?>[] { Channel.class });
			if (tagLostEventRTMethod != null) {
				if (tagLostEventMethod != null) {
					System.err.println("Cannot use both tagLost() and tagLostRT()."); 
				}
				else {			
					RTTagLostEventRegister = true;
					tagLostEventReportChannel = true;
				}
			}
		} catch (Exception e) {
			// function "tagdLostRT(Channel)" not defined
		}
	}

	@Override
	public void pre() {
		if (RTTagEventRegister) {
			RTTagEventRegister = false;
			try {
				if (tagEventReportChannel) { // tagRT(Channel)
					((RFID)device).addTagListener(new RFIDTagListener() {
						public void onTag(RFIDTagEvent e) {
							//System.out.println(e.toString());
							try {
								if (tagEventRTMethod != null) {
									tagEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
								}
							} catch (Exception ex) {
								System.err.println("Disabling tagRT(Channel) for " + deviceType + " because of an error:");
								ex.printStackTrace();
								tagEventRTMethod = null;
							}
						}
					});
				} else { // tagRT()
					((RFID)device).addTagListener(new RFIDTagListener() {
						public void onTag(RFIDTagEvent e) {
							//System.out.println(e.toString());
							try {
								if (tagEventRTMethod != null) {
									tagEventRTMethod.invoke(PAppletParent);
								}
							} catch (Exception ex) {
								System.err.println("Disabling tagRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								tagEventRTMethod = null;
							}
						}
					});
				}
			} catch (Exception ex) {
		    	System.err.println("Disabling tagRT() for " + deviceType + " because of an error:");
		    	ex.printStackTrace();
		    	tagEventRTMethod = null;
		    }
		}
		
		if (RTTagLostEventRegister) {
			RTTagLostEventRegister = false;
			try {
				if (tagLostEventReportChannel) { // tagLostRT(Channel)
					((RFID)device).addTagLostListener(new RFIDTagLostListener() {
						public void onTagLost(RFIDTagLostEvent e) {
							//System.out.println(e.toString());
							try {
								if (tagLostEventRTMethod != null) {
									tagLostEventRTMethod.invoke(PAppletParent, new Object[] { ChannelParent });
								}
							} catch (Exception ex) {
								System.err.println("Disabling tagLostRT(Channel) for " + deviceType + " because of an error:");
								ex.printStackTrace();
								tagLostEventRTMethod = null;
							}
						}
					});
				}
				else {  // tagLostRT
					((RFID)device).addTagLostListener(new RFIDTagLostListener() {
						public void onTagLost(RFIDTagLostEvent e) {
							//System.out.println(e.toString());
							try {
								if (tagLostEventRTMethod != null) {
									tagLostEventRTMethod.invoke(PAppletParent);
								}
							} catch (Exception ex) {
								System.err.println("Disabling tagLostRT() for " + deviceType + " because of an error:");
								ex.printStackTrace();
								tagLostEventRTMethod = null;
							}
						}
					});
				}
			} catch (Exception ex) {
		    	System.err.println("Disabling tagLostRT() for " + deviceType + " because of an error:");
		    	ex.printStackTrace();
		    	tagLostEventRTMethod = null;
		    }
		}
	}
	
	@Override
	public void draw() {
		if (tagFlag) {
			tagFlag = false;
			try {
				if (tagEventMethod != null) {
					if (tagEventReportChannel) {
						tagEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						tagEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling tag() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				tagEventMethod = null;
			}
		}
	
		if (tagLostFlag) {
			tagLostFlag = false;
			try {
				if (tagLostEventMethod != null) {
					if (tagLostEventReportChannel) {
						tagLostEventMethod.invoke(PAppletParent, new Object[] { ChannelParent });
					}
					else {
						tagLostEventMethod.invoke(PAppletParent);
					}
				}
			} catch (Exception ex) {
				System.err.println("Disabling tagLost() for " + deviceType + " because of an error:");
				ex.printStackTrace();
				tagLostEventMethod = null;
			}
		}
	}

	/*
	 * most basic way to use the channel. returns 0 (no tag) or 1 (tag present).
	 * 
	 * @return int
	 */
	@Override
	public int read() {
		try {
			if (((RFID)device).getTagPresent())
				return 1;
			else
				return 0;
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get read value of device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return 0; 
	}

	@Override
	public boolean getTagPresent() {
		try {
			return ((RFID)device).getTagPresent();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get tag present value of device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return false; 
	}

	@Override
	public String getLastTagString() {
		try {
			return ((RFID)device).getLastTag().tagString;
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get last tag string from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return ""; 		
	}
	
	@Override
	public String getLastTagProtocol() {
		try {
			RFIDProtocol prot = ((RFID)device).getLastTag().protocol;
			switch (prot) {
			case EM4100:
				return "EM4100";
			case ISO11785_FDX_B:
				return "ISO11785 FDX B";
			case PHIDGET_TAG:
				return "PhidgetTAG";
			};
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get last tag protocol from device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return ""; 		
	}
	
	@Override
	public void write(String tagString, String prot, boolean lock) {
		try {
			RFIDProtocol protocol = RFIDProtocol.EM4100;
			switch (prot.toUpperCase()) {
			case "EM4100":
				break;
				
			case "ISO11785_FDX_B":
			case "ISO11785": 
				protocol = RFIDProtocol.ISO11785_FDX_B;
				break;
				
			case "PHIDGET_TAG":
			case "PHIDGETTAG": 
				protocol = RFIDProtocol.PHIDGET_TAG;
				break;
				
			default:
				System.err.println("invalid protocol: " + prot + "Protocol can only be \"EM4100\", \"ISO11785_FDX_B\" or \"PHIDGET_TAG\"");
				return;
			}
			
			((RFID)device).write(tagString, protocol, lock);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot write tag - device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
	}
	
	@Override
	public boolean getAntennaEnabled() {
		try {
			return ((RFID)device).getAntennaEnabled();
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot get antenna enabled value of device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
		return false; 
	}
	
	@Override
	public void setAntennaEnabled(boolean ant) {
		try {
			((RFID)device).setAntennaEnabled(ant);
		}
		catch (PhidgetException ex) {
			System.err.println("Cannot set antenna enabled value for device " + deviceType + " because of error: " + ex);
			PAppletParent.exit();
		}
	}
}
