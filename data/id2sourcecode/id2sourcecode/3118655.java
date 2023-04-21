    public void paintOntoClip(Graphics2D g2d, Rectangle rect) {
        try {
            ALayer l = getSelectedLayer();
            for (int i = 0; i < l.getNumberOfChannels(); i++) {
                l.getChannel(i).getGraphicObjects().paintOntoClip(g2d, rect);
            }
        } catch (Exception exc) {
        }
    }
