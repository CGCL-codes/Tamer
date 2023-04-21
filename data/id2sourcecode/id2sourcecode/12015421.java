    public SplashScreen(ImageIcon ghost, SplashCallable callback) {
        this.callback = callback;
        this.ghost = ghost;
        this.addMouseListener(this);
        w = ghost.getIconWidth();
        h = ghost.getIconHeight();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        Rectangle rect = gc.getBounds();
        scrW = rect.width;
        scrH = rect.height;
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e1) {
        }
        int x = (scrW - w) / 2;
        int y = (scrH - h) / 2;
        this.setVisible(true);
        composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);
        screen = robot.createScreenCapture(new Rectangle(x, y, w, h));
        setLocation(x, y);
        pack();
        try {
            Thread.sleep(3000);
            callback.runApplication();
            this.dispose();
        } catch (InterruptedException e) {
        }
    }
