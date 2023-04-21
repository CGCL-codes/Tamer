    public void paintVisel(Graphics2D g2) {
        super.paintVisel(g2);
        double labelX = (x + x2) / 2, labelY = (y + y2) / 2;
        g2.drawChars(label.toCharArray(), 0, label.length(), (int) labelX, (int) (labelY + 20));
    }
