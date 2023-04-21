    private java.awt.image.BufferedImage capture() {
        java.awt.Robot robot;
        try {
            robot = new java.awt.Robot();
        } catch (java.awt.AWTException e) {
            throw new RuntimeException(e);
        }
        java.awt.Rectangle screen = this.getContentPane().getBounds();
        java.awt.Point loc = screen.getLocation();
        SwingUtilities.convertPointToScreen(loc, this.getContentPane());
        screen.setLocation(loc);
        return robot.createScreenCapture(screen);
    }
