    void setDynamicMagnification() {
        c = (dMM - 1) / 2;
        e = (1 + dMM) / 2;
        getOwningView().repaintNow();
    }
