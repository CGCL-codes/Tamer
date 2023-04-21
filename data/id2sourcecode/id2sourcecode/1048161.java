    public TransDemo() {
        ox = 100;
        oy = 100;
        Rectangle bounds = new Rectangle(ox, oy, W, H);
        setBackground(new Color(0, 0, 0, 0));
        setForeground(Color.MAGENTA);
        setBounds(bounds);
        underneath[0] = new BufferedImage(W, H, BufferedImage.TYPE_INT_BGR);
        underneath[1] = new BufferedImage(W, H, BufferedImage.TYPE_INT_BGR);
        try {
            robot = new Robot();
            BufferedImage tmp = robot.createScreenCapture(bounds);
            underneath[0].getGraphics().drawImage(tmp, 0, 0, null);
            underneath[1].getGraphics().drawImage(tmp, 0, 0, null);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        canvas = new BufferedImage(W, H, BufferedImage.TYPE_INT_BGR);
        addMouseMotionListener(this);
        System.gc();
    }
