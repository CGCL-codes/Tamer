    private static BufferedImage capture(JPanel p_panel) {
        Rectangle screen = p_panel.getBounds();
        Point loc = screen.getLocation();
        SwingUtilities.convertPointToScreen(loc, p_panel);
        screen.setLocation(loc);
        Robot robot;
        try {
            robot = new Robot();
            return robot.createScreenCapture(screen);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return null;
    }
