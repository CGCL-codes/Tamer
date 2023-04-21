    public void dissolve(JFrame frame, boolean bExit) throws Exception {
        this.frame = frame;
        this.exit = bExit;
        Robot robot = new Robot();
        Rectangle frameRect = frame.getBounds();
        Dimension frameSize = frame.getSize();
        Point p = frame.getLocationOnScreen();
        frameRect = new Rectangle(p.x, p.y, frameSize.width, frameSize.height);
        frameBuffer = robot.createScreenCapture(frameRect);
        frame.setVisible(false);
        frame.dispose();
        System.gc();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRect = new Rectangle(0, 0, screenSize.width, screenSize.height);
        screenBuffer = robot.createScreenCapture(screenRect);
        fullScreenFrame.getContentPane().setLayout(new BorderLayout());
        fullScreenFrame.getContentPane().add(this, BorderLayout.CENTER);
        fullScreenFrame.setUndecorated(true);
        fullScreenFrame.setBounds(0, 0, screenSize.width, screenSize.height);
        fullScreenFrame.setVisible(true);
        new Thread(this).start();
    }
