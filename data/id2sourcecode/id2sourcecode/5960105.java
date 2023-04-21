    public java.awt.image.BufferedImage getShot() {
        try {
            if (robot == null) {
                robot = new java.awt.Robot();
            }
        } catch (java.awt.AWTException ex) {
            return null;
        }
        if (toolkit == null) {
            toolkit = java.awt.Toolkit.getDefaultToolkit();
        }
        screenSize = toolkit.getScreenSize();
        rectangle = new java.awt.Rectangle(screenSize);
        return robot.createScreenCapture(rectangle);
    }
