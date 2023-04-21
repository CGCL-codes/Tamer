    public void displayChanged(DisplayChangeEvent e) {
        if (vwins == null) return;
        Object source = e.getSource();
        int type = e.getType();
        int value = e.getValue();
        ImagePlus imp;
        ImageWindow iw;
        ImageWindow iwc = WindowManager.getCurrentImage().getWindow();
        if (!iwc.equals(source)) return;
        if (cChannel.getState() && type == DisplayChangeEvent.CHANNEL) {
            for (int n = 0; n < vwins.size(); ++n) {
                imp = getImageFromVector(n);
                if (imp != null) {
                    iw = imp.getWindow();
                    if (!iw.equals(source)) {
                        if (iw instanceof StackWindow) ((StackWindow) iw).setPosition(value, imp.getSlice(), imp.getFrame());
                    }
                }
            }
        }
        if (cSlice.getState() && type == DisplayChangeEvent.Z) {
            for (int n = 0; n < vwins.size(); ++n) {
                imp = getImageFromVector(n);
                if (imp != null) {
                    iw = imp.getWindow();
                    int stacksize = imp.getStackSize();
                    if (!iw.equals(source) && (iw instanceof StackWindow)) {
                        ((StackWindow) iw).setPosition(imp.getChannel(), value, imp.getFrame());
                    }
                }
            }
        }
        if (cFrame.getState() && type == DisplayChangeEvent.T) {
            for (int n = 0; n < vwins.size(); ++n) {
                imp = getImageFromVector(n);
                if (imp != null) {
                    iw = imp.getWindow();
                    if (!iw.equals(source)) {
                        if (iw instanceof StackWindow) ((StackWindow) iw).setPosition(imp.getChannel(), imp.getSlice(), value);
                    }
                }
            }
        }
        ImageCanvas icc = iwc.getCanvas();
        storeCanvasState(icc);
    }
