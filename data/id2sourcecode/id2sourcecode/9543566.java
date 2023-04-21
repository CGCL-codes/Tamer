    private BufferedImage takeCapture(Rectangle rec) throws Exception {
        Robot robot = new Robot();
        return robot.createScreenCapture(rec);
    }
