    public void operateEachChannel(AOperation o) {
        LProgressViewer.getInstance().entrySubProgress(0.5, "layer");
        o.startOperation();
        for (int j = 0; j < getNumberOfChannelSelections(); j++) {
            if (getChannelSelection(j).isSelected()) {
                if (LProgressViewer.getInstance().setProgress(1.0 * (j + 1) / getNumberOfChannelSelections())) return;
                LProgressViewer.getInstance().entrySubProgress(0.3, "channel " + j);
                o.operate(getChannelSelection(j));
                LProgressViewer.getInstance().exitSubProgress();
            }
        }
        o.endOperation();
        System.gc();
        LProgressViewer.getInstance().exitSubProgress();
    }
