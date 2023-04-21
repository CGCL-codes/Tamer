    public void updateArc() {
        double r = layout.getOuterRadius() + layout.getRadiusAdd() * 1 / 2;
        arc.setFrame(-r, -r, 2 * r, 2 * r);
        double start = 2 * Math.PI - layout.getSector(lastNode).getEndAngle();
        double end = 2 * Math.PI - layout.getSector(firstNode).getStartAngle();
        arc.setAngleStart(Math.toDegrees(start));
        arc.setAngleExtent(Math.toDegrees(end - start));
        setBounds(arc.getBounds2D());
        double ang = (start + end) / 2;
        double x = Math.cos(ang) * layout.getInnerRadius() * 0.75;
        double y = -Math.sin(ang) * layout.getInnerRadius() * 0.75;
        if (label != null) {
            label.centerFullBoundsOnPoint(x, y);
            label.setScale(6);
        }
        invalidatePaint();
    }
