    void previousSlice() {
        if (!imp.lock()) return;
        boolean hyperstack = imp.isDisplayedHyperStack();
        int channels = imp.getNChannels();
        int slices = imp.getNSlices();
        int frames = imp.getNFrames();
        if (hyperstack && channels > 1 && !((slices > 1 || frames > 1) && (IJ.controlKeyDown() || IJ.spaceBarDown() || IJ.altKeyDown()))) {
            int c = imp.getChannel() - 1;
            if (c < 1) c = 1;
            swin.setPosition(c, imp.getSlice(), imp.getFrame());
        } else if (hyperstack && slices > 1 && !(frames > 1 && IJ.altKeyDown())) {
            int z = imp.getSlice() - 1;
            if (z < 1) z = 1;
            swin.setPosition(imp.getChannel(), z, imp.getFrame());
        } else if (hyperstack && frames > 1) {
            int t = imp.getFrame() - 1;
            if (t < 1) t = 1;
            swin.setPosition(imp.getChannel(), imp.getSlice(), t);
        } else {
            if (IJ.altKeyDown()) slice -= 10; else slice--;
            if (slice < 1) slice = 1;
            swin.showSlice(slice);
        }
        imp.updateStatusbarValue();
        imp.unlock();
    }
