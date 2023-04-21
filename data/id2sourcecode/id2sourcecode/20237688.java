    private BufferedImage getScreenImage(Rectangle rectangle) {
        Robot robot = null;
        BufferedImage bufferedImage = null;
        try {
            robot = new Robot();
            bufferedImage = robot.createScreenCapture(rectangle);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }
