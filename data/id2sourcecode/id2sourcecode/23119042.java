    public void operateEachChannel(AOperation o) {
        LProgressViewer.getInstance().entrySubProgress(0.7, "clip");
        o.startOperation();
        for (int i = 0; i < getNumberOfLayerSelections(); i++) {
            LProgressViewer.getInstance().entrySubProgress(1.0 / getNumberOfLayerSelections(), "layer " + i);
            for (int j = 0; j < getLayerSelection(i).getNumberOfChannelSelections(); j++) {
                LProgressViewer.getInstance().entrySubProgress(1.0 / getLayerSelection(i).getNumberOfChannelSelections(), "channel " + j);
                if (getLayerSelection(i).getChannelSelection(j).isSelected()) {
                    o.operate(getLayerSelection(i).getChannelSelection(j));
                }
                LProgressViewer.getInstance().exitSubProgress();
            }
            LProgressViewer.getInstance().exitSubProgress();
        }
        o.endOperation();
        System.gc();
        LProgressViewer.getInstance().exitSubProgress();
    }
