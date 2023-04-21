    public void start() {
        super.start();
        ALayer l = getFocussedClip().getSelectedLayer();
        for (int i = 0; i < l.getNumberOfChannels(); i++) {
            l.getChannel(i).getMask().clear();
        }
        repaintFocussedClipEditor();
        updateHistory(GLanguage.translate(getName()));
    }
