    JHumPaletteWindow(int x, int y, int w, int h, JHumPalette npalette) {
        super();
        try {
            Robot rbt = new Robot();
            background = rbt.createScreenCapture(new Rectangle(x, y, w, h));
        } catch (Exception ex) {
            System.out.println(ex);
            System.exit(1);
        }
        add(npalette);
        pack();
        setSize(w, h);
        setLocation(x, y);
        setVisible(true);
    }
