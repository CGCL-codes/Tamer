    public void paintChannelThumbnail(Graphics2D g2d, Rectangle rect, int layerIndex, int channelIndex) {
        ALayer l = getClipModel().getLayer(layerIndex);
        AChannel ch = l.getChannel(channelIndex);
        ch.getPlotter().setRectangle(rect);
        ch.getPlotter().paintBackground(g2d, bgColor);
        ch.getPlotter().paintSamples(g2d, l.getPlotter().getColor(), l.getPlotter().getColorGamma());
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));
        ch.getPlotter().paintFrame(g2d);
    }
