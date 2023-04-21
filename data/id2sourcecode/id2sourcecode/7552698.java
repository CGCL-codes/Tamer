    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK);
        if (type == ZIGZAG) {
            int midY = (srcY + destY) / 2;
            g2d.drawLine(srcX, srcY, srcX, midY);
            g2d.drawLine(srcX, midY, destX, midY);
            g2d.drawLine(destX, midY, destX, destY);
        } else if (type == LINE) {
            g2d.drawLine(srcX, srcY, destX, destY);
        } else {
            if (path != null) {
                g2d.setStroke(stroke);
                g2d.draw(path);
            }
        }
    }
