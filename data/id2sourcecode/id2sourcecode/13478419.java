    private void updateCookie() {
        try {
            ALayer l = getSelectedLayer();
            for (int i = 0; i < l.getNumberOfChannels(); i++) {
                if (l.getChannel(i).getCookies().getCookie(getName()) == null) {
                    AChannel ch = l.getChannel(i);
                    ch.getCookies().setCookie(new Cookie(ch), getName());
                }
            }
        } catch (Exception e) {
        }
    }
