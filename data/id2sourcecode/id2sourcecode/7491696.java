    @Override
    public void paintLegend(final GCWrapper gc) {
        final Rectangle clipping = gc.getClipping();
        final int left = clipping.x;
        final int top = clipping.y;
        final int right = clipping.x + clipping.width;
        final int bottom = clipping.y + clipping.width;
        final int midx = (left + right) / 2;
        gc.drawLine(midx, top, midx, bottom);
    }
