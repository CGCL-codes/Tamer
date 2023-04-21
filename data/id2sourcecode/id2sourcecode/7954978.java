    @Override
    public void draw(Graphics2D g2d) {
        int currentZ = Math.min(atom1.getCurrentZ(), atom2.getCurrentZ());
        int width = (int) (CylinderFactory.MAX_RADIUS / 8 + covalentRadius * currentZ * atom1.getTransform().getScaleFactor());
        int radius = (int) (SphereFactory.MAX_RADIUS / 4 + covalentRadius * currentZ * atom1.getTransform().getScaleFactor());
        if (width < 4) width = 4;
        if (width >= radius) width = radius / 2;
        float x1 = atom1.getCurrentX();
        float y1 = atom1.getCurrentY();
        float x2 = atom2.getCurrentX();
        float y2 = atom2.getCurrentY();
        float mx = (x1 + x2) / 2;
        float my = (y1 + y2) / 2;
        float vx1 = (x1 - mx);
        float vy1 = (y1 - my);
        float vx2 = (x2 - mx);
        float vy2 = (y2 - my);
        float theta = (float) Math.atan(vy1 / vx1);
        if (vx1 < 0.0) theta = THREE_PI_BY_2 + theta; else theta = PI_BY_2 + theta;
        if (sameAtom) {
            vx1 = (x1 - x2);
            vy1 = (y1 - y2);
            g2d.translate(x1, y1);
            g2d.rotate(theta);
            g2d.drawImage(CylinderFactory.getInstance().getCylinderImage(atom1.getAtom().getSymbol()), -(width >> 1), -(width >> 1), width, (int) Math.sqrt(vx1 * vx1 + vy1 * vy1), null);
            g2d.rotate(-theta);
            g2d.translate(-x1, -y1);
        } else {
            g2d.translate(x1, y1);
            g2d.rotate(theta);
            g2d.drawImage(CylinderFactory.getInstance().getCylinderImage(atom1.getAtom().getSymbol()), -(width >> 1), -(width >> 1), width, (int) Math.sqrt(vx1 * vx1 + vy1 * vy1) + 2, null);
            g2d.rotate(-theta);
            g2d.translate(-x1, -y1);
            g2d.translate(mx, my);
            g2d.rotate(theta);
            g2d.drawImage(CylinderFactory.getInstance().getCylinderImage(atom2.getAtom().getSymbol()), -(width >> 1), -(width >> 1), width, (int) Math.sqrt(vx2 * vx2 + vy2 * vy2), null);
            g2d.rotate(-theta);
            g2d.translate(-mx, -my);
            g2d.setTransform(identityTransform);
        }
        if (selected || (atom1.isSelected() && atom2.isSelected())) {
            Stroke oldStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke((float) (width + 2.0), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f));
            g2d.setColor(selectionColor);
            g2d.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
            g2d.setStroke(oldStroke);
        }
    }
