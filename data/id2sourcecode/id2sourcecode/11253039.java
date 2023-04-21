    public static Image printScreen() {
        if (initFailed) return null;
        return ROBOT.createScreenCapture(new Rectangle(SCREEN_SIZE));
    }
