    public void dissolveExit(JFrame frame) {
        try {
            this.frame = frame;
            Robot robot = new Robot();
            Rectangle frame_rect = frame.getBounds();
            frame_buffer = robot.createScreenCapture(frame_rect);
            frame.setVisible(false);
            Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle screen_rect = new Rectangle(0, 0, screensize.width, screensize.height);
            screen_buffer = robot.createScreenCapture(screen_rect);
            fullscreen = new Window(new JFrame());
            fullscreen.setSize(screensize);
            fullscreen.add(this);
            this.setSize(screensize);
            fullscreen.setVisible(true);
            new Thread(this).start();
        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }
