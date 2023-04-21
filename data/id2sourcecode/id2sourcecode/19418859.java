    public void createScreenCapture(Rectangle screenRect, File destFile) {
        commandsToRun.add(new RobotCommand(RobotMethod.CREATE_SCREEN_CAPTURE, screenRect, destFile));
    }
