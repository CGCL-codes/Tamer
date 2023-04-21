    private c a(final int i1, final int j1, final boolean flag, final Graphics g1) {
        new c();
        final FontMetrics fontmetrics = g1.getFontMetrics();
        final int k1 = g1.getFont().getSize() / 2;
        final int l1 = fontmetrics.getHeight() - fontmetrics.getDescent();
        final int i2 = fontmetrics.getDescent();
        if (flag) {
            final int j2 = (j1 - l1) + 1;
            final int k2 = (j1 + i2) - 1;
            final int l2 = (j2 + k2) / 2;
            if (D.w.equals("<")) {
                g1.drawLine(i1 + k1, j2, i1, l2);
                g1.drawLine(i1, l2, i1 + k1, k2);
            } else {
                g1.drawLine(i1, j2, i1 + k1, l2);
                g1.drawLine(i1 + k1, l2, i1, k2);
            }
        }
        return new c(k1, l1, i2);
    }
