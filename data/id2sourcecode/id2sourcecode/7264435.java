    public void mouseMoved(MouseEvent e) {
        try {
            switch(tab.getSelectedIndex()) {
                case 0:
                    segments = ((GCookieFreeGenerator) getSelectedLayer().getChannel(e.getPoint()).getCookies().getCookie(getName())).segments;
                    segments.mouseMoved(e);
                    break;
                case 1:
                    freehand = ((GCookieFreeGenerator) getSelectedLayer().getChannel(e.getPoint()).getCookies().getCookie(getName())).freehand;
                    freehand.mouseMoved(e);
                    break;
            }
            reloadGui();
            repaintFocussedClipEditor();
        } catch (Exception exc) {
        }
    }
