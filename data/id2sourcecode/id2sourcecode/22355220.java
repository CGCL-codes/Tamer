    public PopeyeDialog(java.awt.Frame f, boolean b) {
        super(f, b);
        if (f != null) {
            this.setLocation(f.getX() + f.getWidth() / 3, f.getY() + f.getHeight());
        }
        setUndecorated(true);
        try {
            r = new Robot();
            transparent = r.createScreenCapture(rect);
        } catch (AWTException awe) {
            awe.printStackTrace();
            System.out.println("error reading screen");
            System.exit(0);
        }
        jpl = new PopeyeDialogBackgroundPanel(transparent, scaledBitmap, this);
        jpl.setOpaque(false);
        setContentPane(jpl);
        requestFocus();
        this.FrameMover = new PopeyeDialogMover(this);
        this.addMouseListener(FrameMover);
        this.addMouseMotionListener(FrameMover);
    }
