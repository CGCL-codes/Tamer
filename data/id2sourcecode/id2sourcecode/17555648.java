    public void start() {
        super.start();
        ALayer l = getFocussedClip().getSelectedLayer();
        for (int i = 0; i < l.getNumberOfChannels(); i++) {
            AChannel ch = l.getChannel(i);
            AChannelSelection s = ch.getSelection();
            int o = s.getOffset() + s.getLength();
            ch.modifySelection(o, ch.getSampleLength() - o);
        }
        updateHistory(GLanguage.translate(getName()));
        repaintFocussedClipEditor();
    }
