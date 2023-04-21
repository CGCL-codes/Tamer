    public void paintOntoClip(Graphics2D g2d, Rectangle rect) {
        try {
            ALayer l = getSelectedLayer();
            for (int i = 0; i < l.getNumberOfChannels(); i++) {
                ((Cookie) l.getChannel(i).getCookies().getCookie(getName())).area.paintOntoClip(g2d, rect);
            }
        } catch (Exception exc) {
        }
    }
