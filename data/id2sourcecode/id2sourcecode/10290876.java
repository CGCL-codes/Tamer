    @Override
    public void paintTo(Graphics2D g2, int x, int y, int width, int height, boolean down, String text) {
        g2.drawImage(topLeft, x, y, null);
        g2.drawImage(topRight, x + width - topRight.getWidth(), y, null);
        g2.drawImage(bottomLeft, x, y + height - bottomLeft.getHeight(), null);
        g2.drawImage(bottomRight, x + width - bottomRight.getWidth(), y + height - bottomRight.getHeight(), null);
        Paint save = g2.getPaint();
        g2.setPaint(new TexturePaint(topCenter, new Rectangle(x, y, topCenter.getWidth(), topCenter.getHeight())));
        g2.fillRect(x + topLeft.getWidth(), y, width - topLeft.getWidth() - topRight.getWidth(), topCenter.getHeight());
        g2.setPaint(new TexturePaint(bottomCenter, new Rectangle(x, y + height - bottomLeft.getHeight(), bottomCenter.getWidth(), bottomCenter.getHeight())));
        g2.fillRect(x + bottomLeft.getWidth(), y + height - bottomLeft.getHeight(), width - bottomLeft.getWidth() - bottomRight.getWidth(), bottomCenter.getHeight());
        g2.setPaint(new TexturePaint(leftMiddle, new Rectangle(x, y + topLeft.getHeight(), leftMiddle.getWidth(), leftMiddle.getHeight())));
        g2.fillRect(x, y + topLeft.getHeight(), leftMiddle.getWidth(), height - topLeft.getHeight() - bottomLeft.getHeight());
        g2.setPaint(new TexturePaint(rightMiddle, new Rectangle(x + width - rightMiddle.getWidth(), y + topLeft.getHeight(), rightMiddle.getWidth(), rightMiddle.getHeight())));
        g2.fillRect(x + width - rightMiddle.getWidth(), y + topRight.getHeight(), rightMiddle.getWidth(), height - topRight.getHeight() - bottomRight.getHeight());
        if (down) {
            GradientPaint grad = new GradientPaint(new Point(x + 5, y + 7), new Color(0xFF7a3c5d), new Point(x + 5, y + height - 14), new Color(0xFF4f6cb5));
            g2.setPaint(grad);
            g2.fillRect(x + 5, y + 7, width - 9, height - 14);
        } else {
            GradientPaint grad = new GradientPaint(new Point(x + 3, y + 6), new Color(0xFF7a3c5d), new Point(x + 3, y + height - 12), new Color(0xFF4f6cb5));
            g2.setPaint(grad);
            g2.fillRect(x + 3, y + 6, width - 5, height - 12);
        }
        g2.setPaint(save);
        FontMetrics fm = g2.getFontMetrics();
        int tw = fm.stringWidth(text);
        int th = fm.getHeight();
        int tx = x + (width - tw) / 2;
        int ty = y + (height - th) / 2;
        g2.setColor(new Color(0x509090));
        g2.drawString(text, tx + 1, ty + 1 + fm.getAscent());
        g2.setColor(Color.BLACK);
        g2.drawString(text, tx, ty + fm.getAscent());
        if (down) {
            g2.setColor(new Color(0, 0, 0, 92));
            g2.fillRect(x + 3, y + 3, width - 5, 4);
            g2.fillRect(x + 3, y + 7, 4, height - 9);
        }
    }
