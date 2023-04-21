    private void updateCookie() {
        try {
            ALayer l = getSelectedLayer();
            for (int i = 0; i < l.getNumberOfChannels(); i++) {
                if (l.getChannel(i).getCookies().getCookie(getName()) == null) {
                    AChannel ch = l.getChannel(i);
                    ch.getCookies().setCookie(new GCookieFreeGenerator(ch), getName());
                }
            }
            segments = ((GCookieFreeGenerator) getSelectedLayer().getSelectedChannel().getCookies().getCookie(getName())).segments;
            freehand = ((GCookieFreeGenerator) getSelectedLayer().getSelectedChannel().getCookies().getCookie(getName())).freehand;
        } catch (Exception e) {
        }
    }
