    void drawShape() {
        Graphics2D g2 = menu.buff.g2;
        if (isOpen()) g2.setPaint(getStyle().getGradient(MenuItem.OVER, x - rHi, y - rHi, x + rHi, y + rHi)); else g2.setPaint(getStyle().getGradient(getState(), x - rHi, y - rHi, x + rHi, y + rHi));
        g2.fill(wedge);
        g2.setStroke(getStroke());
        g2.setPaint(getStrokeColor());
        g2.draw(wedge);
        if (items.size() > 0 && !isOpen()) {
            float theta = (tLo + tHi) / 2;
            float scale = (rHi - rLo) / 2;
            float dx = (float) (Math.cos(theta) * scale / 4f);
            float dy = (float) (Math.sin(theta) * scale / 4f);
            AffineTransform at = AffineTransform.getTranslateInstance(outerX + dx, outerY + dy);
            at.scale(scale, scale);
            at.rotate(theta);
            Area tri = (Area) getStyle().get("subTriangle");
            Area newTri = tri.createTransformedArea(at);
            g2.setPaint(getStrokeColor());
            g2.fill(newTri);
        }
    }
