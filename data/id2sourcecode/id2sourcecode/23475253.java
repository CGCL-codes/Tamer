    private void takePhoto(Rectangle rect) {
        try {
            Robot robot = new Robot();
            img = robot.createScreenCapture(rect);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
