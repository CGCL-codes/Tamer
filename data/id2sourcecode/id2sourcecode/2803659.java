    void deleteHyperstackSliceOrFrame() {
        int channels = imp.getNChannels();
        int slices = imp.getNSlices();
        int frames = imp.getNFrames();
        int c1 = imp.getChannel();
        int z1 = imp.getSlice();
        int t1 = imp.getFrame();
        if (frames > 1 && slices == 1) deleteFrames = true; else if (frames == 1 && slices > 1) deleteFrames = false; else if (slices > 1 && frames > 1) {
            GenericDialog gd = new GenericDialog("Delete Slice");
            gd.addCheckbox("Delete time point " + t1, deleteFrames);
            gd.showDialog();
            if (gd.wasCanceled()) return;
            deleteFrames = gd.getNextBoolean();
        } else return;
        if (!imp.lock()) return;
        ImageStack stack = imp.getStack();
        if (deleteFrames) {
            for (int z = slices; z >= 1; z--) {
                int index = imp.getStackIndex(channels, z, t1);
                for (int i = 0; i < channels; i++) stack.deleteSlice(index - i);
            }
            frames--;
        } else {
            for (int t = frames; t >= 1; t--) {
                int index = imp.getStackIndex(channels, z1, t);
                for (int i = 0; i < channels; i++) stack.deleteSlice(index - i);
            }
            slices--;
        }
        imp.setDimensions(channels, slices, frames);
        imp.unlock();
    }
