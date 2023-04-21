    public void paintLinkToDoorIfAny(Graphics g, int srcX, int srcY, int dstX, int dstY, double viewZoom, double viewXOffset, double viewYOffset) {
        if (getAssociatedPanel() instanceof PathPanel) {
            PathPanel pp = (PathPanel) getAssociatedPanel();
            ItemNode door = pp.getDoor();
            if (door != null) {
                int centerX = (srcX + dstX) / 2;
                int centerY = (srcY + dstY) / 2;
                int doorX = (int) door.getBounds().getCenterX();
                int doorY = (int) door.getBounds().getCenterY();
                int viewDoorX = (int) (((int) doorX - viewXOffset) * viewZoom);
                int viewDoorY = (int) (((int) doorY - viewYOffset) * viewZoom);
                Color oldColor = g.getColor();
                g.setColor(Color.LIGHT_GRAY);
                g.drawLine(centerX, centerY, viewDoorX, viewDoorY);
                g.setColor(oldColor);
            }
        }
    }
