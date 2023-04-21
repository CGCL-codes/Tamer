    @Override
    public Insets getBorderInsets(final Component c) {
        int t, b, l, r;
        t = b = l = r = 5;
        final int width = c.getWidth();
        final int height = c.getHeight();
        if (width < 10) {
            l = width / 2;
            r = (width + 1) / 2;
        }
        if (height < 10) {
            t = height / 2;
            b = (height + 1) / 2;
        }
        return new Insets(t, l, b, r);
    }
