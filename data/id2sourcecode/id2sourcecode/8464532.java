    private void initBackground(Dimension size) {
        try {
            Robot robot = new Robot();
            background = robot.createScreenCapture(new Rectangle(new Point(0, getYOffset()), size));
        } catch (AWTException e) {
            GuiUtil.handleProblem(e);
        }
    }
