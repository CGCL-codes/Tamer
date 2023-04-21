    void drawEndCap(int x, int y, int diameter, Color color, Color outline) {
        int radiusCap, xUpperLeft, yUpperLeft;
        radiusCap = (diameter + 1) / 2;
        xUpperLeft = x - radiusCap;
        yUpperLeft = y - radiusCap;
        switch(styleBond) {
            case DisplayControl.QUICKDRAW:
                --diameter;
                g.setColor(color);
                g.fillOval(xUpperLeft, yUpperLeft, diameter, diameter);
                g.setColor(outline);
                g.drawOval(xUpperLeft, yUpperLeft, diameter, diameter);
                break;
            case DisplayControl.SHADING:
                if (shadedSphereRenderer == null) shadedSphereRenderer = new ShadedSphereRenderer(control);
                shadedSphereRenderer.render(g, xUpperLeft, yUpperLeft, diameter, color, outline);
        }
    }
