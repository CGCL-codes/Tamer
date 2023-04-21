    protected void updatePicker() {
        if (pickerFrame != null && pickerFrame.isShowing()) {
            PointerInfo info = MouseInfo.getPointerInfo();
            Point mouseLoc = info.getLocation();
            pickerFrame.setLocation(mouseLoc.x - pickerFrame.getWidth() / 2, mouseLoc.y - pickerFrame.getHeight() / 2);
            pickLoc.x = mouseLoc.x + pickOffset.x;
            pickLoc.y = mouseLoc.y + pickOffset.y;
            if (pickLoc.x >= 0 && pickLoc.y >= 0) {
                Color c = robot.getPixelColor(pickLoc.x, pickLoc.y);
                if (!c.equals(previousColor) || !mouseLoc.equals(previousLoc)) {
                    previousColor = c;
                    previousLoc = mouseLoc;
                    captureRect.setLocation(mouseLoc.x + captureOffset.x, mouseLoc.y + captureOffset.y);
                    if (captureRect.x >= 0 && captureRect.y >= 0) {
                        BufferedImage capture = robot.createScreenCapture(captureRect);
                        cursorGraphics.setComposite(AlphaComposite.Src);
                        cursorGraphics.setColor(transparentColor);
                        cursorGraphics.fillRect(0, 0, cursorImage.getWidth(), cursorImage.getHeight());
                        cursorGraphics.setColor(Color.red);
                        cursorGraphics.fillOval(glassRect.x, glassRect.y, glassRect.width, glassRect.height);
                        cursorGraphics.setComposite(AlphaComposite.SrcIn);
                        cursorGraphics.drawImage(capture, zoomRect.x, zoomRect.y, zoomRect.width, zoomRect.height, this);
                        cursorGraphics.setComposite(AlphaComposite.SrcOver);
                        cursorGraphics.drawImage(magnifierImage, 0, 0, this);
                        BufferedImage subImage = cursorImage.getSubimage(0, 0, cursorImage.getWidth(), cursorImage.getHeight());
                        pickerFrame.setCursor(getToolkit().createCustomCursor(cursorImage, hotSpot, "ColorPicker"));
                    }
                }
            }
        }
    }
