    public void paint(Graphics gr, int x, int y, boolean filled) {
        if (x != firstX || y != firstY) {
            int x1 = Math.min(x, firstX);
            int x2 = Math.max(x, firstX);
            int y1 = Math.min(y, firstY);
            int y2 = Math.max(y, firstY);
            int w2 = (x1 + x2) / 2;
            int h2 = (y1 + y2) / 2;
            gr.drawLine(x1, h2, w2, y1);
            gr.drawLine(w2 + 1, y1, x2, h2);
            gr.drawLine(x1, h2, w2, y2);
            gr.drawLine(w2 + 1, y2, x2, h2);
        }
    }
