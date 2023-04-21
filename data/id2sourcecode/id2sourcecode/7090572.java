    public void paint(Graphics g) {
        if (image.getColorModel().hasAlpha()) {
            try {
                Robot robot = new Robot();
                BufferedImage fond = robot.createScreenCapture(getBounds());
                MediaTracker tracker = new MediaTracker(this);
                tracker.addImage(fond, 0);
                tracker.waitForAll();
                g.drawImage(fond, 0, 0, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        g.drawImage(image, 0, 0, null);
    }
