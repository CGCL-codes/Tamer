    public void loadLeftImage(Rectangle rectangle) {
        try {
            Robot robot = new Robot();
            image = robot.createScreenCapture(rectangle);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
