    protected void paintHorizontal(GraphRegion rgn, int y, int height, double max, double min) {
        super.paintHorizontal(rgn, y, height, max, min);
        int textW = rgn.fm.stringWidth(text), textH = rgn.fm.getHeight();
        if (rgn.width > textW + 5 && height > textH + 4) {
            rgn.gr.setColor(textColor);
            double v = (max + min) / 2;
            rgn.gr.drawString(text, rgn.x + (rgn.width - textW) / 2, rgn.y - (int) (rgn.scaleY * (v - rgn.minY)) + textH / 3);
        }
    }
