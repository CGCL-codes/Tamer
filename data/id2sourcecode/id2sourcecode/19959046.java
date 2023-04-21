    public void lock() {
        try {
            Robot robot = new Robot();
            Rectangle bounds = getRootPane().getBounds();
            BufferedImage screen = robot.createScreenCapture(new Rectangle(getX() + bounds.x, getY() + bounds.y, getWidth(), getHeight()));
            ((GlassPane) getGlassPane()).setScreen(screen);
        } catch (AWTException e) {
            GuiUtils.showError(e);
        }
        getGlassPane().setVisible(true);
    }
