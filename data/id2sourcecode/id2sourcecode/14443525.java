    public void takeSnapShot() {
        Point origin = mainPanel.getLocationOnScreen();
        final Rectangle rect = new Rectangle(origin.x, origin.y, mainPanel.getWidth(), mainPanel.getHeight());
        while (AWTUtilities.getWindowOpacity(this) != 0.0f) {
            try {
                AWTUtilities.setWindowOpacity(this, 0.0f);
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(CaptureFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        BufferedImage capI = robot.createScreenCapture(rect);
        AWTUtilities.setWindowOpacity(this, normalOpacity);
        if (listener != null) listener.onFrameCapture(capI, key);
    }
