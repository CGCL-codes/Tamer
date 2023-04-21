    public BufferedImage takeDesktopScreenshot() {
        Rectangle screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        JTextComponent textComponent = findFocusOwnerAndHideItsCaret();
        try {
            return robot.createScreenCapture(screen);
        } finally {
            if (textComponent != null) showCaretOf(textComponent);
        }
    }
