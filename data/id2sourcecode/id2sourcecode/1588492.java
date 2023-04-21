    public ColorPicker() {
        try {
            robot = new Robot();
            robot.createScreenCapture(new Rectangle(0, 0, 1, 1));
        } catch (AWTException e) {
            throw new AccessControlException("Unable to capture screen");
        }
    }
