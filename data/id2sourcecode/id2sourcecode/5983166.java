    public ALayerSelection(ALayerSelection s) {
        this((ALayer) s.model);
        this.name = s.name;
        for (int i = 0; i < s.getNumberOfChannelSelections(); i++) {
            addChannelSelection(new AChannelSelection(s.getChannelSelection(i)));
        }
    }
