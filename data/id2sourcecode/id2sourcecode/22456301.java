    public BufferedImage takeScreenshotOf(Component c) {
        Point locationOnScreen = locationOnScreen(c);
        Dimension size = sizeOf(c);
        Rectangle r = new Rectangle(locationOnScreen.x, locationOnScreen.y, size.width, size.height);
        JTextComponent textComponent = findFocusOwnerAndHideItsCaret();
        if (textComponent != null) robot.waitForIdle();
        try {
            return robot.createScreenCapture(r);
        } finally {
            if (textComponent != null) {
                showCaretOf(textComponent);
                robot.waitForIdle();
            }
        }
    }
