    @Override
    public void paintBorder(final Component c, final Graphics g, final int x, final int y, final int width, final int height) {
        int left = 5;
        int right = 5;
        int top = 5;
        int bottom = 5;
        if (width < 10) {
            left = width / 2;
            right = (width + 1) / 2;
        }
        if (height < 10) {
            top = height / 2;
            bottom = (height + 1) / 2;
        }
        final Graphics graphics = g.create();
        graphics.setColor(COLOR);
        int r_start = width - (right * 2);
        int b_start = height - (bottom * 2);
        graphics.fillArc(0, 0, left * 2, top * 2, 90, 90);
        graphics.fillArc(r_start, 0, right * 2, top * 2, 0, 90);
        graphics.fillArc(r_start, b_start, right * 2, bottom * 2, 270, 90);
        graphics.fillArc(0, b_start, left * 2, bottom * 2, 180, 90);
        graphics.fillRect(left, 0, width - (right + left), top);
        graphics.fillRect(width - right, top, right, height - (top + bottom));
        graphics.fillRect(left, height - bottom, width - (left + right), bottom);
        graphics.fillRect(0, top, left, height - (top + bottom));
    }
