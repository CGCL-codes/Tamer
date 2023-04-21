    public void setDimensions(int nChannels, int nSlices, int nFrames) {
        if (nChannels * nSlices * nFrames != getImageStackSize() && ip != null) {
            nChannels = 1;
            nSlices = getImageStackSize();
            nFrames = 1;
            if (isDisplayedHyperStack()) {
                setOpenAsHyperStack(false);
                new StackWindow(this);
                setSlice(1);
            }
        }
        boolean updateWin = isDisplayedHyperStack() && (this.nChannels != nChannels || this.nSlices != nSlices || this.nFrames != nFrames);
        this.nChannels = nChannels;
        this.nSlices = nSlices;
        this.nFrames = nFrames;
        if (updateWin) {
            if (nSlices != getImageStackSize()) setOpenAsHyperStack(true);
            ip = null;
            img = null;
            setPositionWithoutUpdate(getChannel(), getSlice(), getFrame());
            if (isComposite()) ((CompositeImage) this).reset();
            new StackWindow(this);
            setPosition(getChannel(), getSlice(), getFrame());
        }
    }
