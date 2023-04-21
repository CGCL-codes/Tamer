    public void operateLayer0WithLayer1And2(AOperation o) {
        int n0 = getLayerSelection(0).getNumberOfChannelSelections();
        int n1 = getLayerSelection(1).getNumberOfChannelSelections();
        int n2 = getLayerSelection(1).getNumberOfChannelSelections();
        int n = Math.min(Math.min(n0, n1), n2);
        LProgressViewer.getInstance().entrySubProgress(0.7, "clip");
        o.startOperation();
        for (int i = 0; i < n; i++) {
            if (LProgressViewer.getInstance().setProgress(1.0 * (i + 1) / n)) return;
            if (getLayerSelection(0).getChannelSelection(i).isSelected()) {
                o.operate(getLayerSelection(0).getChannelSelection(i), getLayerSelection(1).getChannelSelection(i), getLayerSelection(2).getChannelSelection(i));
            }
        }
        o.endOperation();
        System.gc();
        LProgressViewer.getInstance().exitSubProgress();
    }
