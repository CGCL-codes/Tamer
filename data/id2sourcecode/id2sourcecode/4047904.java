    BufferedImage TakeScreen() throws AWTException {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        screenResolution = new Rectangle(0, 0, screenSize.width, screenSize.height);
        Robot robot = new Robot();
        oldScreen = currentScreen;
        currentScreen = robot.createScreenCapture(screenResolution);
        return currentScreen;
    }
