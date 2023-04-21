    public AboutView() {
        ghost = ResourceHandler.getImage("note.png");
        this.addMouseListener(this);
        this.getContentPane().setLayout(null);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        Rectangle rect = gc.getBounds();
        this.setSize(ghost.getIconWidth(), ghost.getIconHeight());
        Toolkit kit = Toolkit.getDefaultToolkit();
        this.setLocation(((int) kit.getScreenSize().getWidth() - this.getWidth()) / 2, ((int) kit.getScreenSize().getHeight() - this.getHeight()) / 2);
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e1) {
        }
        composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);
        screen = robot.createScreenCapture(new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight()));
    }
