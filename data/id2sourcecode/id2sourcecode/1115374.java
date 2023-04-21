    private void resetSnapScreen() {
        setVisible(false);
        if (robot == null) {
            try {
                robot = new Robot();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        BufferedImage screenImage = robot.createScreenCapture(screenRect);
        setVisible(true);
        snapArea.setScreenImage(screenImage);
    }
