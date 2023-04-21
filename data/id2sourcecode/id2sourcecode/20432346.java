    protected void translateDiscs(DiscItemRecord[] discs) {
        double left = 0;
        double right = 0;
        double top = 0;
        double bottom = 0;
        for (int i = 0; i < discs.length; i++) {
            DiscItemRecord disc = discs[i];
            left = Math.min(left, disc.getX() - disc.getR());
            right = Math.max(right, disc.getX() + disc.getR());
            top = Math.min(top, disc.getY() - disc.getR());
            bottom = Math.max(bottom, disc.getY() + disc.getR());
        }
        double xMid = (left + right) / 2;
        double yMid = (top + bottom) / 2;
        double xScale = discDataArea.getWidth() / (right - left);
        double yScale = discDataArea.getHeight() / (bottom - top);
        scale = Math.min(xScale, yScale);
        double dx = discDataArea.getCenterX() - xMid * scale;
        double dy = discDataArea.getCenterY() - yMid * scale;
        for (int i = 0; i < discs.length; i++) {
            discs[i].setTranslation(dx, dy, scale);
        }
    }
