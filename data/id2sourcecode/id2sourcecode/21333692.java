    public void mergeDownLayer(int index) {
        if ((getNumberOfElements() > 1) && (index > 0)) {
            ALayer l0 = getLayer(index - 1);
            ALayer l1 = getLayer(index);
            ALayerSelection s0 = l0.createSelection();
            ALayerSelection s1 = l1.createSelection();
            AClipSelection c = new AClipSelection(this);
            c.addLayerSelection(s0);
            c.addLayerSelection(s1);
            c.operateLayer0WithLayer1(new AOMix());
            remove(index);
            for (int i = 0; i < l0.getNumberOfChannels(); i++) {
                l0.getChannel(i).getMask().clear();
            }
        }
    }
