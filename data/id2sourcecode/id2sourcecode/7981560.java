    @Override
    public void paintGraphics(BasicMap map, Graphics g) {
        boolean show = this.parseBoolean("show", true);
        if (!show) return;
        boolean ids = this.parseBoolean("ids", false);
        boolean conn = this.parseBoolean("show connections", false);
        boolean connNo = this.parseBoolean("show connections #s", false);
        boolean randomColor = this.parseBoolean("random color", true);
        boolean areaSize = this.parseBoolean("area size", false);
        int opacity = this.parseInt("opacity", 180);
        Color c = Color.black;
        if (!randomColor) {
            c = this.parseColor("color", Color.red);
            c = new Color(c.getRed(), c.getGreen(), c.getBlue(), opacity);
        }
        int x, y, x2, y2;
        for (Block b : blocks) {
            if (randomColor) c = getRandomColor(opacity);
            g.setColor(c);
            if (!map.isVisible(b.x_mean, b.y_mean)) continue;
            g.fillPolygon(getPolygon(map, b));
            g.setColor(g.getColor().darker().darker().darker());
            x = map.getXonPanel(b.x_mean);
            y = map.getYonPanel(b.y_mean);
            if (ids) g.drawString("Block: " + b.id, x + 10, y + 10);
            x += 2;
            y += 2;
            int counter = 0;
            int rOff = (int) map.getZoom();
            if (conn || connNo) {
                for (Node n : b.border) {
                    x2 = map.getXonPanel(n);
                    y2 = map.getYonPanel(n);
                    if (conn) g.drawLine(x, y, x2, y2);
                    if (connNo) {
                        int mX = (x + x2) / 2;
                        int mY = (y + y2) / 2;
                        g.drawString(" " + ++counter, mX, mY);
                    }
                }
            }
            if (areaSize) {
                x = map.getXonPanel(b.x_mean);
                y = map.getYonPanel(b.y_mean);
                g.drawString("area: " + b.area_size, x, y);
            }
        }
    }
