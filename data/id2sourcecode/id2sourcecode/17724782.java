    public void setCharWidth(double w, String from) {
        if (charWidth == 0) {
            charWidth = w;
        }
        if (w != charWidth) {
            int startRes = getStartRes();
            int endRes = getEndRes();
            double charWidth = getCharWidth();
            int currentWidth = (int) ((endRes - startRes + 1) * charWidth);
            int centreRes = (startRes + endRes) / 2;
            int prevstart = startRes;
            startRes = centreRes - (int) (currentWidth / (2 * w));
            endRes = centreRes + (int) (currentWidth / (2 * w));
            if (startRes < 0) {
                startRes = 0;
                endRes = startRes + (int) (currentWidth / w);
            }
            this.startRes = startRes;
            this.endRes = endRes;
            this.charWidth = w;
        }
    }
