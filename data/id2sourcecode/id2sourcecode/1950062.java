    public Console() {
        super();
        propertySupport = new PropertyChangeSupport(this);
        setUndecorated(true);
        try {
            r = new Robot();
            transparent = r.createScreenCapture(rect);
        } catch (AWTException awe) {
            awe.printStackTrace();
            System.out.println("error reading screen");
            System.exit(0);
        }
        jpl = new ConsoleBackgroundPanel(transparent, scaledBitmap, this);
        jpl.setOpaque(false);
        setContentPane(jpl);
        requestFocus();
        addFocusListener(new FocusAdapter() {

            public void focusLost(FocusEvent fe) {
                transparent = null;
            }

            public void focusGained(FocusEvent fe) {
            }
        });
        this.FrameMover = new FrameMover(this);
        this.addMouseListener(FrameMover);
        this.addMouseMotionListener(FrameMover);
    }
