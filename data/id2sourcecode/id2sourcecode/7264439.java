    public void paintOntoClip(Graphics2D g2d, Rectangle rect) {
        try {
            ALayer l = getSelectedLayer();
            for (int i = 0; i < l.getNumberOfChannels(); i++) {
                switch(tab.getSelectedIndex()) {
                    case 0:
                        ((GCookieFreeGenerator) l.getChannel(i).getCookies().getCookie(getName())).segments.paintOntoClip(g2d, rect);
                        break;
                    case 1:
                        ((GCookieFreeGenerator) l.getChannel(i).getCookies().getCookie(getName())).freehand.paintOntoClip(g2d, rect);
                        break;
                }
            }
        } catch (Exception exc) {
        }
    }
