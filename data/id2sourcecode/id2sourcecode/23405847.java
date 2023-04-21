    public BufferedImage createScreenCapture(Rectangle screenRect) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new AWTPermission("readDisplayPixels"));
        }
        if (screenRect.isEmpty()) {
            throw new IllegalArgumentException(Messages.getString("awt.13D"));
        }
        return nativeRobot.createScreenCapture(screenRect);
    }
