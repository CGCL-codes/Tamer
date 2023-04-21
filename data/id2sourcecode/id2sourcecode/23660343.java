    public void start() {
        MLogger.debug("fade", "start");
        Container contentPane = getContentPane();
        if (contentPane == null) return;
        Point location = contentPane.getLocation();
        SwingUtilities.convertPointToScreen(location, contentPane);
        Dimension size = contentPane.getSize();
        Rectangle rectangle = new Rectangle(location.x, location.y, size.width, size.height);
        if (rectangle.isEmpty()) {
            MLogger.debug("fade", "Empty content pane (?)");
            return;
        }
        try {
            background = new Robot().createScreenCapture(rectangle);
        } catch (Exception exception) {
            MLogger.exception(exception);
            return;
        }
        foreground = (BufferedImage) contentPane.createImage(size.width, size.height);
        if (foreground == null) return;
        Graphics g = foreground.createGraphics();
        contentPane.paint(g);
        g.dispose();
        active = true;
        timer = new MTimer(FADE_TIMEOUT) {

            @Override
            protected void onTimeout() {
                Fade.this.doFade();
            }
        };
        timer.start();
    }
