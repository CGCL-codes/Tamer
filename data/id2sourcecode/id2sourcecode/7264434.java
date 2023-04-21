    public void mousePressed(MouseEvent e) {
        try {
            switch(tab.getSelectedIndex()) {
                case 0:
                    segments = ((GCookieFreeGenerator) getSelectedLayer().getChannel(e.getPoint()).getCookies().getCookie(getName())).segments;
                    segments.mousePressed(e);
                    break;
                case 1:
                    freehand = ((GCookieFreeGenerator) getSelectedLayer().getChannel(e.getPoint()).getCookies().getCookie(getName())).freehand;
                    freehand.mousePressed(e);
                    break;
            }
            reloadGui();
            repaintFocussedClipEditor();
        } catch (Exception exc) {
        }
    }
