    public MySplashScreen(String path) throws IOException, AWTException {
        final URL url = this.getClass().getResource(path);
        image = ImageIO.read(url);
        currentImage = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(image.getWidth(null), image.getHeight(null));
        final Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        final int x = (int) (screenDimension.getWidth() / 2 - image.getWidth(null) / 2);
        final int y = (int) (screenDimension.getHeight() / 2 - image.getHeight(null) / 2);
        final int w = image.getWidth(null);
        final int h = image.getHeight(null);
        final Robot robot = new Robot();
        final Rectangle rectangle = new Rectangle(x, y, w, h);
        background = robot.createScreenCapture(rectangle);
        drawImage(0f);
        label = new SplashPainter();
        label.setImage(background);
        final JWindow f = new JWindow(new JFrame());
        f.getContentPane().add(label);
        f.pack();
        f.setLocationRelativeTo(null);
        timer = new Timer(speed, this);
        timer.setCoalesce(true);
        timer.start();
        startTime = System.currentTimeMillis();
        f.setAlwaysOnTop(true);
        f.setVisible(true);
    }
