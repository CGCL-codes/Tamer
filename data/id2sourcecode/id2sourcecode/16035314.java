    private void captureScreenShot() {
        try {
            setSize(0, 0);
            Robot robot = new Robot();
            Rectangle rect = new Rectangle(x, y, imgWidth, imgHeight);
            screenShot = robot.createScreenCapture(rect);
            setSize(imgWidth, imgHeight);
            repaint();
        } catch (java.awt.AWTException ex) {
            ex.printStackTrace();
        }
    }
