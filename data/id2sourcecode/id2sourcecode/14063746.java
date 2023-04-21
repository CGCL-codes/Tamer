    public Screen(ScreenInfo info) {
        _info = info;
        try {
            Rectangle size = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            Robot robot = new Robot();
            BufferedImage screen = robot.createScreenCapture(size);
            _img = new BufferedImage(screen.getWidth(), screen.getHeight(), BufferedImage.TYPE_INT_ARGB);
            for (int y = 0; y < screen.getHeight(); y++) {
                for (int x = 0; x < screen.getWidth(); x++) {
                    _img.setRGB(x, y, screen.getRGB(x, y));
                }
            }
        } catch (AWTException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
