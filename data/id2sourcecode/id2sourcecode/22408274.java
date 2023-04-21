    public static void makeSnapshot(JWindow window) {
        try {
            Robot robot = new Robot();
            int x = window.getX();
            int y = window.getY();
            Dimension dim = window.getPreferredSize();
            hShadowBg = robot.createScreenCapture(new Rectangle(x, y + dim.height - 5, dim.width, 5));
            vShadowBg = robot.createScreenCapture(new Rectangle(x + dim.width - 5, y, 5, dim.height - 5));
        } catch (AWTException e) {
            clearSnapshot();
        }
    }
