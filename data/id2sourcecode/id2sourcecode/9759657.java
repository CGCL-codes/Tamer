    private void renderCDS(Graphics2D g, Feature rf) {
        int middle = 4 * lineHeight + tickHeight / 2;
        boolean featureSelected = model.getFeatureSelection().contains(rf);
        Location last = null;
        int lastY = 0;
        for (Location l : rf.location()) {
            int drawFrame = getDrawFrame(l, rf);
            int lmin = translateGenomeToScreen(l.start(), model.getAnnotationLocationVisible());
            int lmax = translateGenomeToScreen(l.end() + 1, model.getAnnotationLocationVisible());
            int hor;
            if (rf.strand() == Strand.REVERSE) hor = middle + (drawFrame * lineHeight) + tickHeight / 2 + gap; else hor = middle - lineHeight - (drawFrame * lineHeight) - tickHeight / 2 + gap;
            int height = lineHeight - 2 * gap;
            Rectangle r = new Rectangle(lmin, hor, lmax - lmin, height);
            Color cdsColor = Configuration.getColor("TYPE_CDS");
            g.setColor(new Color(cdsColor.getRed(), cdsColor.getGreen(), cdsColor.getBlue(), 20));
            g.fill(r);
            g.setColor(Color.BLACK);
            g.draw(r);
            boolean locationSelected = model.getLocationSelection().contains(l);
            if (locationSelected) {
                g.setStroke(new BasicStroke(2.0f));
                g.setColor(Color.BLACK);
                g.draw(r);
                g.setStroke(new BasicStroke(1.0f));
            }
            if (featureSelected) {
                g.setColor(new Color(0, 0, 1, 0.5f));
                g.fill(r);
            }
            if (last != null) {
                int lastX = translateGenomeToScreen(last.end() + 1, model.getAnnotationLocationVisible());
                int currentX = translateGenomeToScreen(l.start(), model.getAnnotationLocationVisible());
                int currentY = hor + height / 2;
                int maxY = Math.min(currentY, lastY) - height / 2;
                int middleX = (lastX + currentX) / 2;
                g.setColor(Color.BLACK);
                g.drawLine(lastX, lastY, middleX, maxY);
                g.drawLine(middleX, maxY, currentX, currentY);
            }
            super.collisionMap.addLocation(r, l);
            last = l;
            lastY = hor + height / 2;
        }
    }
