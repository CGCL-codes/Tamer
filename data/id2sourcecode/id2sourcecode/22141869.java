    public void start() {
        super.start();
        ALayer l = getFocussedClip().getSelectedLayer();
        for (int i = 0; i < l.getNumberOfChannels(); i++) {
            AChannel ch = l.getChannel(i);
            ch.modifySelection(0, ch.getSelection().getOffset());
        }
        updateHistory(GLanguage.translate(getName()));
        repaintFocussedClipEditor();
    }
