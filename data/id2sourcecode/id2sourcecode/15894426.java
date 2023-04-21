    private Transframe(String path) throws AWTException {
        this.path = path;
        setLocation(0, 0);
        setUndecorated(true);
        setSize(screenSize);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Robot robot = new Robot();
        image = robot.createScreenCapture(rectangle);
        addKeyListener(new MyKeyListener(this, robot));
        MyMouseListener myMouseListener = new MyMouseListener(this);
        addMouseListener(myMouseListener);
        addMouseMotionListener(myMouseListener);
        setVisible(true);
    }
