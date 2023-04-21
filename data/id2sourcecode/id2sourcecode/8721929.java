    public static Dimension findTemplateFullScreen(Template template) {
        Dimension dim = null;
        dim = Toolkit.getDefaultToolkit().getScreenSize();
        BufferedImage img = null;
        Robot robot;
        try {
            robot = new Robot();
            img = robot.createScreenCapture(new Rectangle(1, 1, (int) dim.getWidth(), (int) dim.getHeight()));
            for (int x = 0; x < (int) dim.getWidth() - template.getWidth(); x++) for (int y = 0; y < (int) dim.getHeight() - template.getHeight(); y++) {
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return dim;
    }
