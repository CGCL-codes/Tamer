    public void paintPlayPointer(Graphics2D g2d, Rectangle rect, Color color) {
        xPlayPointer = audio.getClip().getSelectedLayer().getChannel(0).getPlotter().sampleToGraphX(audio.getPlayPointer());
        int y0 = (int) rect.getX();
        int y1 = (int) (rect.getX() + rect.getHeight());
        g2d.setClip(rect);
        g2d.setStroke(new BasicStroke());
        g2d.setColor(color);
        g2d.drawLine(xPlayPointer, y0, xPlayPointer, y1);
    }
