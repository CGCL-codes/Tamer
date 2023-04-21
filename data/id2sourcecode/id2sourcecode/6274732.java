    public static HSL RGB2HSL(final Color c1) {
        float themin;
        float themax;
        float delta;
        final HSL c2 = new HSL();
        themin = Math.min(c1.r, Math.min(c1.g, c1.b));
        themax = Math.max(c1.r, Math.max(c1.g, c1.b));
        delta = themax - themin;
        c2.l = (themin + themax) / 2;
        c2.s = 0;
        if (c2.l > 0 && c2.l < 1) {
            c2.s = delta / (c2.l < 0.5 ? (2 * c2.l) : (2 - 2 * c2.l));
        }
        c2.h = 0;
        if (delta > 0) {
            if (themax == c1.r && themax != c1.g) {
                c2.h += (c1.g - c1.b) / delta;
            }
            if (themax == c1.g && themax != c1.b) {
                c2.h += (2 + (c1.b - c1.r) / delta);
            }
            if (themax == c1.b && themax != c1.r) {
                c2.h += (4 + (c1.r - c1.g) / delta);
            }
            c2.h *= 60;
        }
        return (c2);
    }
