    public void mouseMoved(MouseEvent e) {
        try {
            actualMask = getSelectedLayer().getChannel(e.getPoint()).getMask();
            if (actualMask.getSegments() != null) {
                actualMask.getSegments().mouseMoved(e);
            }
            repaintFocussedClipEditor();
        } catch (Exception exc) {
        }
    }
