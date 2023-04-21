    protected void paintComponent(Graphics gOrig) {
        super.paintComponent(gOrig);
        Graphics2D g = (Graphics2D) gOrig;
        PreferenceParams prefs = PreferenceParams.getInstance();
        Dimension d = this.getSize();
        int start = 0;
        int centre = 0;
        northPole = 0;
        southPole = 0;
        eastPole = 0;
        westPole = 0;
        if (prefs.centring) {
            hidePen = true;
            draw(g, animal.getAtom(), initParams(), centre, start, centre, start);
            hidePen = false;
            midriff = (southPole + northPole) / 2;
            verticalOffset = start - midriff;
        }
        hidePen = false;
        g.translate(d.width / 2, d.height / 2);
        if (prefs.sideways) {
            g.rotate(Math.PI / 2.0d);
            int temp = d.width;
            d.width = d.height;
            d.height = temp;
        }
        g.setColor(Color.BLACK);
        g.drawRect(0 - d.width / 2, 0 - d.height / 2, d.width, d.height);
        draw(g, animal.getAtom(), initParams(), centre, start + verticalOffset, centre, start + verticalOffset);
        hidePen = true;
    }
