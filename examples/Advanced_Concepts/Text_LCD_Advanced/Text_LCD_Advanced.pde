/*
 This example demonstrates updating custom characters on an LCD. You can update up to 8 characters on most LCD boards. In order to display them, use e.g. \u0001 inside
 the text String to be displayed, as demonstrated below
*/

import shenkar.SimplePhidgets.*;

Channel myLCD;

byte[] swanL = new byte[] {
        0,0,1,0,0,
        0,1,1,1,0,
        0,0,0,1,0,
        0,0,1,0,0,
        0,1,0,0,1,
        1,1,1,1,1,
        0,1,1,1,1,
        0,0,1,1,1
      };

byte[] swanR = new byte[] {
        0,0,0,0,0,
        0,0,0,0,0,
        0,0,0,0,0,
        0,1,1,1,0,
        1,1,1,0,0,
        1,1,1,0,0,
        1,1,0,0,0,
        1,0,0,0,0
      };

void setup() {
  size(400, 400);
  myLCD = new Channel(this, "1203"); 
  myLCD.setBacklight(0.7);
  myLCD.setCharacterBitmap("\u0001", swanL);  // loading bitmap for left character of the swan
  myLCD.setCharacterBitmap("\u0002", swanR);  // loading bitmap for right character of the swan
}

void draw() {
  if (mousePressed) {
    myLCD.clear();
    myLCD.writeText(0,0,"There are swans in");
    myLCD.writeText(0,1,"the lake! \u0001\u0002");  // write the text to bottom line, including the 2 custom characters of the swan 
    myLCD.flush();
  }
  else {
    myLCD.clear();
    myLCD.writeText(0,0,"Good morning World!");
    myLCD.flush();
  }    
}

/*
 All functions for an LCD channel:

 flush() - send to the display all changes made to text or graphics since last call to flush()
 
 writeText(int x, int y, String text) - write the text to the screen (call flush() after to make the difference appear)
 clear() - delete all characters (or graphics for relevant boards) from the LCD. NOTICE: this will not effect the LCD until the flush() function is called
 
 setBackLight(float) - sets the intensity of the LCD's back light (0..1)
 getBackLight() - returns curernt intensity of the LCD's back light (0..1, float)
 getMinBacklight() - minimum value for back light (float)
 getMaxBacklight() - maximum value for back light (float)
 
 setContrast(float) - sets the contrast of the display (0..1)
 getContrast() - returns current contrast (0..1, float)
 getMinContrast() - minimum value for contrast (float)
 getMaxContrast() - maximum value for contrast (float)
 
 setCursorBlink(boolean) - sets periodically blinking of the cursor at last location of writing to the LCD
 getCursorBlink() - returns status of cursor blinking (boolean)
 
 setCursorOn(boolean) - sets an underline at current curor position
 getCursorOn() - returns status of cursor display (boolean)
 
 setCharacterBitmap(String character, byte[] bitmap) - set a custom character. see example above.
 getMaxCharacters() - get maximum characters that can fit in the LCD (all rows and columns) (int)

 getHeight() - returns height of the dislay in rows (for text LCDs) or pixels (for graphics LCDs) (int)
 getWidth() - returns width of the dislay in columns (for text LCDs) or pixels (for graphics LCDs) (int)
 
 getScreenSize() - returns screen size as String (e.g. "2X20")
 
 
  Specific functions for the PhidgetTextLCD Adapter (1204):
  
 setScreenSize(String) - tell the board which LCD screen is connected. Options are "noScreen", "1x8", "2x8", "1x16", "2x16", "4x16", "2x20", "4x20", 
   "2x24", "1x40", "2x40", "4x40" or "64x128"
 initialize() - initializes the display (this is underdocumented in the Phidget's website and should probably be called after setting display size)
 
 
  Specific functions for the LCD1100 Graphic LCD Phidget:
 
 setFrameBuffer() - frame buffer to use - commands sent to the device are performed on this buffer.
 getFrameBuffer() - returns the frame buffer that is currently being used. Commands sent to the device are performed on this buffer.
 saveFrameBuffer(int buffer) - save the buffer to flash memory so that it stays on the device even after disconnecting it from power.
  NOTE: Use this function with cautious, for the flash memory has limited writings lifetime.
        for more information: https://www.phidgets.com/?view=api&lang=Java&api=LCD
 
 drawLine(int x1, int y1, int x2, int y2) - draws a line between two points
 drawPixel(int x, int y, String pixelState) - draws a single pixel. pixelState may be "on", "off" or "invert"
 drawRect(int x1, int y1, int x2, int y2, boolean filled, boolean inverted) - draws a rectangle
 writeBitmap(int xPosition, int yPosition, int xSize, int ySize, byte[] bitmap) - draws a bitmap to screen. The bitmap should be an array of 0 or 1 bytes.
 copy(int sourceFramebuffer, int destFramebuffer, int sourceX1, int sourceY1, int sourceX2, int sourceY2, int destX, int destY, boolean inverted) -
   copy a rectangular region in the display to a different buffer and/or location
 
 setFontSize(String fontName, int width, int height) - before loading a user font, you need to specify character's size for it in pixels
  Possible font names: "user1", "user2", "6x10", "5x8" or "6x12"
 setCharacterBitmap(String fontName, String character, byte[] bitmap) - same as above version of the function, but with the opotion to specify "user1" or "user2" as font to use
  It is recommended to read the user guide for using custom characters with LCD1100. see the "How to Write Custom Characters" in the User Guide tab at https://www.phidgets.com/?prodid=963
 getMaxCharacters(String fontName) - get maximum characters that can fit in the LCD (all rows and columns) for the specified font
 writeText(String fontName, int x, int y, String text) - same as above, but allows to choose the font to use
 
 setSleeping(boolean) -  putting the device to sleep turns off the display and backlight in order to save power.
 getSleeping() - returns current sleeping state (boolean)
*/
