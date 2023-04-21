    private void dumpAWT(Container container, BufferedImage image) {
        for (int i = 0; i < container.getComponentCount(); i++) {
            Component child = container.getComponent(i);
            if (!(child instanceof JComponent)) {
                Rectangle bounds = child.getBounds();
                Point location = bounds.getLocation();
                bounds.setLocation(child.getLocationOnScreen());
                BufferedImage capture = robot.createScreenCapture(bounds);
                bounds.setLocation(location);
                SwingUtilities.convertRectangle(child, bounds, getContentPane());
                image.createGraphics().drawImage(capture, location.x, location.y, this);
                if (child instanceof Container) {
                    dumpAWT(container, image);
                }
            }
        }
    }
