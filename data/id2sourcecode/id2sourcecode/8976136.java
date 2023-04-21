    private void renderCDS(Graphics2D g, Feature rf) {
        Color[] background = new Color[] { new Color(204, 238, 255, 100), new Color(255, 255, 204, 100), new Color(204, 238, 255, 100) };
        int lineHeight = (int) (0.9 * (this.getHeight() / 3.0));
        double posPixelRatio = (double) (this.getWidth() * 0.90) / (double) rf.length();
        int hGap = (int) (this.getWidth() * 0.05);
        int vGap = (int) (this.getHeight() * 0.05);
        g.setColor(background[0]);
        g.fillRect(0, 0, this.getWidth(), lineHeight + vGap);
        g.setColor(background[1]);
        g.fillRect(0, lineHeight + vGap, this.getWidth(), lineHeight);
        g.setColor(background[0]);
        g.fillRect(0, 2 * lineHeight + vGap, this.getWidth(), lineHeight + vGap);
        Location last = null;
        int lastY = 0;
        int arrowDrawFrame = -1;
        for (Location l : rf.location()) {
            int drawFrame = getDrawFrame(l, rf);
            if (rf.strand() == Strand.REVERSE && last == null) arrowDrawFrame = drawFrame; else if (rf.strand() == Strand.FORWARD) arrowDrawFrame = drawFrame;
            int lmin = (int) ((l.start() - rf.start()) * posPixelRatio);
            int lmax = (int) ((l.end() - rf.start() + 1) * posPixelRatio);
            int hor;
            hor = ((drawFrame - 1) * lineHeight);
            Rectangle r = new Rectangle(lmin + hGap, hor + vGap, lmax - lmin, lineHeight);
            Color cdsColor = Configuration.getColor("TYPE_CDS");
            if (SequenceTools.hasInternalStopCodon(entry.sequence, rf, l, model.getAAMapping())) {
                g.setColor(Color.RED);
            } else {
                g.setColor(cdsColor);
            }
            g.fill(r);
            g.setColor(cdsColor.darker());
            g.draw(r);
            g.setStroke(new BasicStroke(4.0f));
            if (SequenceTools.missingDonor(entry.sequence, rf, l)) {
                g.setColor(Color.RED);
                if (rf.strand() == Strand.FORWARD) g.drawLine(r.x + r.width, r.y, r.x + r.width, r.y + r.height); else g.drawLine(r.x, r.y, r.x, r.y + r.height);
            }
            if (SequenceTools.missingAcceptor(entry.sequence, rf, l)) {
                g.setColor(Color.RED);
                if (rf.strand() == Strand.REVERSE) g.drawLine(r.x + r.width, r.y, r.x + r.width, r.y + r.height); else g.drawLine(r.x, r.y, r.x, r.y + r.height);
            }
            g.setStroke(new BasicStroke(1.0f));
            if (last != null) {
                int lastX = (int) ((last.end() - rf.start() + 1) * posPixelRatio);
                int currentX = (int) ((l.start() - rf.start()) * posPixelRatio);
                int currentY = hor + lineHeight / 2;
                int maxY = Math.min(currentY, lastY) - lineHeight / 2;
                int middleX = (lastX + currentX) / 2;
                g.setColor(Color.BLACK);
                g.drawLine(lastX + hGap, lastY + vGap, middleX + hGap, maxY + vGap);
                g.drawLine(middleX + hGap, maxY + vGap, currentX + hGap, currentY + vGap);
            }
            collisionMap.addLocation(r, l);
            last = l;
            lastY = hor + lineHeight / 2;
        }
        int hor = ((arrowDrawFrame - 1) * lineHeight) + vGap;
        int arrowLenght = 10;
        if (rf.strand() == Strand.FORWARD) {
            int x = (int) (this.getWidth() * 0.95);
            g.drawLine(x, hor, x + arrowLenght, hor + (lineHeight / 2));
            g.drawLine(x + arrowLenght, hor + (lineHeight / 2), x, hor + lineHeight);
        } else {
            int x = (int) (this.getWidth() * 0.05);
            g.drawLine(x, hor, x - arrowLenght, hor + (lineHeight / 2));
            g.drawLine(x - arrowLenght, hor + (lineHeight / 2), x, hor + lineHeight);
        }
        Location l = model.getAnnotationLocationVisible();
        int lmin = (int) ((l.start() - rf.start()) * posPixelRatio);
        int lmax = (int) ((l.end() - rf.start() + 1) * posPixelRatio);
        Rectangle r = new Rectangle(lmin + hGap - 1, 0, lmax - lmin + 1, this.getHeight() - 1);
        g.setColor(Color.RED);
        g.draw(r);
    }
