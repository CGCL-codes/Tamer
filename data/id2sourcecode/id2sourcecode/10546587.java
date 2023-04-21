    public static GeneralPath getWindowShape(int low, int lowFade, int high, int highFade) {
        GeneralPath p = new GeneralPath();
        int lhc = (low + lowFade) - (high - highFade);
        p.moveTo(low, 127);
        if (lhc > 0 && lowFade != 0 && highFade != 0) {
            int hfx = high - highFade;
            int lfx = low + lowFade;
            int hfy = (127 * (hfx - low)) / lowFade;
            p.lineTo(hfx, 127 - hfy);
            int midx = hfx + (lfx - hfx) / 2;
            int midy = (127 * (high - midx) / highFade);
            p.lineTo(midx, 127 - midy);
            int lfy = (127 * (high - lfx)) / highFade;
            p.lineTo(lfx, 127 - lfy);
        } else {
            p.lineTo(low + lowFade, 0);
            p.lineTo(high - highFade, 0);
        }
        p.lineTo(high, 127);
        p.closePath();
        return p;
    }
