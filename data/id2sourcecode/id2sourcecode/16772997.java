    public static BufferedImage captureImage(Robot robot, Component component) {
        Rectangle bounds = component.getBounds();
        java.awt.Point point = new java.awt.Point(bounds.x, bounds.y);
        SwingUtilities.convertPointToScreen(point, component);
        bounds.setBounds(point.x, point.y, bounds.width, bounds.height);
        return robot.createScreenCapture(bounds);
    }
