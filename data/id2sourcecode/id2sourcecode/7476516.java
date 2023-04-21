    public VGraphics(Graphics g, Dimension rd, Dimension vd, int _dpi) {
        gd = g;
        dpi = _dpi;
        if (rd == null) {
            Rectangle r;
            r = g.getClipBounds();
            rd = new Dimension(r.width, r.height);
        }
        if (vd == null) vd = new Dimension(rd.width, rd.height);
        xScale = ((float) rd.width) / ((float) vd.width);
        yScale = ((float) rd.height) / ((float) vd.height);
        avgScale = (xScale + yScale) / 2;
        scaling = ((xScale != 1.0) || (yScale != 1.0));
        rWidth = rd.width;
        rHeight = rd.height;
        vWidth = vd.width;
        vHeight = vd.height;
    }
