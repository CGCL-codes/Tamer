    public void dissolveExit(JFrame frame) {
        try {
            this.frame = frame;
            Robot robot = new Robot();
            Rectangle frame_rectangle = frame.getBounds();
            frameBuffer = robot.createScreenCapture(frame_rectangle);
            frame.setVisible(false);
            Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle screen_rect = new Rectangle(0, 0, screensize.width, screensize.height);
            screenBuffer = robot.createScreenCapture(screen_rect);
            fullscreen = new Window(new JFrame());
            fullscreen.setSize(screensize);
            fullscreen.add(this);
            this.setSize(screensize);
            fullscreen.setVisible(true);
            new Thread(this).start();
        } catch (AWTException awt) {
            LOG.log(Level.WARNING, "Dissolve problem ", awt);
        }
    }
