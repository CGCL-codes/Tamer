    public void run() {
        try {
            Thread.yield();
            Toolkit tk = Toolkit.getDefaultToolkit();
            tk.sync();
            Rectangle r = new Rectangle(tk.getScreenSize());
            Robot robot = new Robot();
            scr = robot.createScreenCapture(r);
        } catch (Exception ex) {
            System.out.println(ex);
            scr = null;
        }
        if (entireScreen) {
            jp.scr = scr;
            if (image) jp.doAction("xxImage for Insertion"); else jp.doAction("xxBackground for Insertion");
            return;
        }
        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
        repaint(1l);
    }
