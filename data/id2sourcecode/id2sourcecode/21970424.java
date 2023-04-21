    public void paintLoopPointer(Graphics2D g2d, Rectangle rect, Color color) {
        int y0 = (int) rect.getX();
        int y1 = (int) (rect.getX() + rect.getHeight());
        int h = 5;
        g2d.setClip(rect);
        g2d.setStroke(new BasicStroke());
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2d.setColor(color);
        xLoopStartPointer = audio.getClip().getSelectedLayer().getChannel(0).getPlotter().sampleToGraphX(audio.getLoopStartPointer());
        g2d.drawLine(xLoopStartPointer, y0, xLoopStartPointer, y1);
        xLoopEndPointer = audio.getClip().getSelectedLayer().getChannel(0).getPlotter().sampleToGraphX(audio.getLoopEndPointer());
        g2d.drawLine(xLoopEndPointer, y0, xLoopEndPointer, y1);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2d.fillRect(xLoopStartPointer, y1 - h, xLoopEndPointer - xLoopStartPointer, h);
    }
