    public TransparentBackground(String title) {
        super(title);
        try {
            System.out.println((GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDefaultConfiguration()).getBounds());
            background = (new Robot()).createScreenCapture(new Rectangle(0, 0, 1280, 1024));
            addComponentListener(this);
            addWindowFocusListener(this);
        } catch (AWTException e) {
        }
    }
