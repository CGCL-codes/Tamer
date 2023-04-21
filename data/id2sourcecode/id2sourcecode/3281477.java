    @CallBackScript
    public Object shot() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        Rectangle screenRect = new Rectangle(screenSize);
        Robot robot;
        try {
            robot = new Robot();
            BufferedImage image = robot.createScreenCapture(screenRect);
            show(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
