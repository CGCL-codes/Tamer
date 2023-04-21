    public static void capture() {
        animationEnabled = false;
        setFocusable(false);
        sleep(100);
        if (robby != null) robby.waitForIdle();
        JFrame canvasFrame = new JFrame("Jaguar v" + version + " canvasFrame");
        canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container topContentPane = canvasFrame.getContentPane();
        JPanel panel = new JPanel();
        Canvas c = new Canvas(canvasFrame.getGraphicsConfiguration());
        c.setBackground(CANVASCOLOR);
        c.setSize(screen.width, screen.height);
        panel.setBackground(CANVASCOLOR);
        panel.setSize(screen.width, screen.height);
        panel.add(c);
        topContentPane.add(panel);
        canvasFrame.setUndecorated(true);
        canvasFrame.pack();
        canvasFrame.setVisible(true);
        sleep(300);
        canvasFrame.toFront();
        canvasFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        canvasFrame.setLocation(0, 0);
        canvasFrame.setSize(screen.width, screen.height);
        sleep(600);
        if (canvas == null) {
            canvas = getRows("canvas", robby.createScreenCapture(screen));
        }
        swap(1);
        sleep(1000);
        screen = getRows("screen", robby.createScreenCapture(screen));
        canvasFrame.setVisible(false);
        canvasFrame.dispose();
        animationEnabled = true;
    }
