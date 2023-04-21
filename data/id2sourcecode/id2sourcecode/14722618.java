    public BufferedImage getImage() {
        BufferedImage img = null;
        try {
            Toolkit tk = Toolkit.getDefaultToolkit();
            tk.sync();
            Rectangle screen = new Rectangle(tk.getScreenSize());
            Robot robot = new Robot();
            robot.setAutoDelay(0);
            robot.setAutoWaitForIdle(false);
            long it = System.currentTimeMillis();
            img = robot.createScreenCapture(screen);
            if (m_bDebug) System.out.println("BufferedImage::getImage(): t=" + (System.currentTimeMillis() - it));
        } catch (Exception exc) {
            System.out.println("DesktopCapture::getImage(): " + exc);
        }
        return img;
    }
