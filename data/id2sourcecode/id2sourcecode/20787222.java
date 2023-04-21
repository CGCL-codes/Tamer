    private void prepareAnimation(Point location) {
        if (animationImage != null) {
            return;
        }
        pack();
        Dimension size = getSize();
        if (mode.needsBackgroundImage() && backgroundImage == null) {
            try {
                Robot robot = new Robot();
                backgroundImage = robot.createScreenCapture(new Rectangle(location, size));
            } catch (AWTException e) {
                LOG.warn("Could not capture background image", e);
                backgroundImage = null;
            }
        }
        animationImage = getGraphicsConfiguration().createCompatibleImage(size.width, size.height);
        Graphics grahpics = animationImage.getGraphics();
        getContentPane().paint(grahpics);
        grahpics.dispose();
    }
