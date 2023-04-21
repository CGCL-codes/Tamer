    protected Font fontThatFitsAll(Graphics2D context, double maxWidth, double maxHeight) {
        if (log.isInfoEnabled()) log.info("looking for font that fits.");
        if (maxWidth <= 0 || maxHeight <= 0) return null;
        int big = 128;
        int small = 4;
        Font best = null;
        best = testFont(small, context, maxWidth, maxHeight);
        if (best == null) return null;
        Font f = testFont(big, context, maxWidth, maxHeight);
        if (f != null) return f;
        int t = (big + small) / 2;
        while (t != small) {
            f = testFont(t, context, maxWidth, maxHeight);
            if (f != null) {
                small = t;
                best = f;
            } else {
                big = t;
            }
            t = (big + small) / 2;
        }
        return best;
    }
