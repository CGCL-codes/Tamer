    public void mousePressed(MouseEvent e) {
        actualMask = getSelectedLayer().getChannel(e.getPoint()).getMask();
        actualMask.getSegments().mousePressed(e);
        repaintFocussedClipEditor();
    }
