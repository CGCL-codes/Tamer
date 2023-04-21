    private void renderCDS(Graphics2D g, Feature rf, int yOffset) {
        int middle = 4 * lineHeight + tickHeight / 2;
        boolean featureSelected = model.selectionModel().getFeatureSelection().contains(rf);
        Location last = null;
        int lastY = 0;
        for (int i = 0; i < rf.location().length; i++) {
            Location l = rf.location()[i];
            int drawFrame = getDrawFrame(i, l, rf);
            int lmin = Convert.translateGenomeToScreen(l.start(), model.getAnnotationLocationVisible(), screenWidth);
            int lmax = Convert.translateGenomeToScreen(l.end() + 1, model.getAnnotationLocationVisible(), screenWidth);
            int hor;
            if (rf.strand() == Strand.REVERSE) hor = middle + (drawFrame * lineHeight) + tickHeight / 2; else hor = middle - lineHeight - (drawFrame * lineHeight) - tickHeight / 2;
            int height = lineHeight;
            Rectangle r = new Rectangle(lmin, hor + yOffset, lmax - lmin, height);
            Color cdsColor = Configuration.getColor("TYPE_CDS");
            g.setColor(new Color(cdsColor.getRed(), cdsColor.getGreen(), cdsColor.getBlue(), 20));
            g.fill(r);
            g.setColor(Color.BLACK);
            g.draw(r);
            boolean locationSelected = model.selectionModel().getLocationSelection().contains(l);
            if (locationSelected) {
                g.setStroke(new BasicStroke(2.0f));
                g.setColor(Color.BLACK);
                g.draw(r);
                g.setStroke(new BasicStroke(1.0f));
                g.setColor(new Color(1f, 0.5f, 0, 0.5f));
                g.fill(r);
            } else if (featureSelected) {
                g.setColor(new Color(0, 0, 1, 0.5f));
                g.fill(r);
            }
            if (last != null) {
                int lastX = Convert.translateGenomeToScreen(last.end() + 1, model.getAnnotationLocationVisible(), screenWidth);
                int currentX = Convert.translateGenomeToScreen(l.start(), model.getAnnotationLocationVisible(), screenWidth);
                int currentY = hor + height / 2;
                int maxY = Math.min(currentY, lastY) - height / 2;
                int middleX = (lastX + currentX) / 2;
                g.setColor(Color.BLACK);
                g.drawLine(lastX, lastY + yOffset, middleX, maxY + yOffset);
                g.drawLine(middleX, maxY + yOffset, currentX, currentY + yOffset);
            }
            r.y -= yOffset;
            collisionMap.addLocation(r, l);
            last = l;
            lastY = hor + height / 2;
        }
    }
