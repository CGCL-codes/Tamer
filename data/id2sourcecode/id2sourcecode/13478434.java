    private void onInverse(ActionEvent e) {
        updateActualArea();
        if (GToolkit.isCtrlKey(e)) {
            ALayer l = getSelectedLayer();
            for (int i = 0; i < l.getNumberOfChannels(); i++) {
                ((Cookie) l.getChannel(i).getCookies().getCookie(getName())).area.setInversed(inversed.isSelected());
            }
        } else {
            area.setInversed(inversed.isSelected());
        }
        repaintFocussedClipEditor();
    }
