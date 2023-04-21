    private void doStart() {
        Dimension screen;
        Rectangle rect;
        try {
            this.setVisible(false);
            Thread.sleep(500);
            Toolkit tk = Toolkit.getDefaultToolkit();
            screen = tk.getScreenSize();
            rect = new Rectangle(0, 0, screen.width, screen.height);
            screenImage = robot.createScreenCapture(rect);
            JFrame jf = new JFrame();
            Temp temp = new Temp(jf, screenImage, screen.width, screen.height);
            jf.getContentPane().add(temp, BorderLayout.CENTER);
            jf.setUndecorated(true);
            jf.setSize(screen);
            jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jf.setVisible(true);
            jf.setAlwaysOnTop(true);
        } catch (Exception exe) {
            exe.printStackTrace();
        } finally {
            screen = null;
            rect = null;
            if (screenImage != null) {
                screenImage.flush();
                screenImage = null;
            }
        }
    }
