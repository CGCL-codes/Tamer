    int colorize(int r, int g, int b, int a) {
        if (cbri == 100) {
            return colorToInt(255, 255, 255, a);
        } else if (cbri == -100) {
            return colorToInt(0, 0, 0, a);
        }
        int hi1 = r;
        if (g >= r && g >= b) hi1 = g; else if (b >= r && b >= g) hi1 = b;
        int lo1 = r;
        if (g <= r && g <= b) lo1 = g; else if (b <= r && b <= g) lo1 = b;
        int grey = (hi1 + lo1) / 2;
        if (preserveGrey) {
            if (r == g && r == b) {
                return colorToInt(grey, grey, grey, a);
            }
        }
        if (cbri < 0) {
            grey += grey * cbri / 100;
        } else if (cbri > 0) {
            grey += (255 - grey) * cbri / 100;
        }
        int hr = 0;
        int hg = 0;
        int hb = 0;
        int diff = 0;
        if (grey >= 127) {
            diff = 255 - grey;
        } else {
            diff = grey;
        }
        if (hiIsR) {
            hr = grey + diff * csat / 100;
        } else if (hiIsG) {
            hg = grey + diff * csat / 100;
        } else if (hiIsB) {
            hb = grey + diff * csat / 100;
        }
        if (mdIsR) {
            if (grey >= 127) {
                diff = fr + (255 - fr) * (grey - 127) / 128 - grey;
            } else {
                diff = fr * grey / 127 - grey;
            }
            hr = grey + diff * csat / 100;
        } else if (mdIsG) {
            if (grey >= 127) {
                diff = fg + (255 - fg) * (grey - 127) / 128 - grey;
            } else {
                diff = fg * grey / 127 - grey;
            }
            hg = grey + diff * csat / 100;
        } else if (mdIsB) {
            if (grey >= 127) {
                diff = fb + (255 - fb) * (grey - 127) / 128 - grey;
            } else {
                diff = fb * grey / 127 - grey;
            }
            hb = grey + diff * csat / 100;
        }
        diff = grey - (255 - grey);
        if (diff < 0) diff = 0;
        diff = grey - diff;
        if (loIsR) {
            hr = grey - diff * csat / 100;
        } else if (loIsG) {
            hg = grey - diff * csat / 100;
        } else if (loIsB) {
            hb = grey - diff * csat / 100;
        }
        return colorToInt(hr, hg, hb, a);
    }
