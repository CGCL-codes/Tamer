    public void dissolveExit(JFrame frame) {
        try {
            this.frame = frame;
            Robot robot = new Robot();
            Rectangle frame_rect = frame.getBounds();
            frame_buffer = robot.createScreenCapture(frame_rect);
            frame.setVisible(false);
            Dimension screensize = new Dimension(frame.getWidth(), frame.getHeight());
            screen_buffer = robot.createScreenCapture(frame.getBounds());
            fullscreen = new Window(new JFrame());
            fullscreen.setBounds(frame.getBounds());
            fullscreen.add(this);
            frame.setVisible(true);
            fullscreen.setVisible(true);
            new Thread(this).start();
        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }
