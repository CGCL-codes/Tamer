    ImagePlus resliceHyperstack(ImagePlus imp) {
        int channels = imp.getNChannels();
        int slices = imp.getNSlices();
        int frames = imp.getNFrames();
        if (slices == 1) {
            IJ.error("Reslice...", "Cannot reslice z=1 hyperstacks");
            return null;
        }
        int c1 = imp.getChannel();
        int z1 = imp.getSlice();
        int t1 = imp.getFrame();
        int width = imp.getWidth();
        int height = imp.getHeight();
        ImagePlus imp2 = null;
        ImageStack stack2 = null;
        Roi roi = imp.getRoi();
        for (int t = 1; t <= frames; t++) {
            for (int c = 1; c <= channels; c++) {
                ImageStack tmp1Stack = new ImageStack(width, height);
                for (int z = 1; z <= slices; z++) {
                    imp.setPositionWithoutUpdate(c, z, t);
                    tmp1Stack.addSlice(null, imp.getProcessor());
                }
                ImagePlus tmp1 = new ImagePlus("tmp", tmp1Stack);
                tmp1.setCalibration(imp.getCalibration());
                tmp1.setRoi(roi);
                ImagePlus tmp2 = reslice(tmp1);
                int slices2 = tmp2.getStackSize();
                if (imp2 == null) {
                    imp2 = tmp2.createHyperStack("Reslice of " + imp.getTitle(), channels, slices2, frames, tmp2.getBitDepth());
                    stack2 = imp2.getStack();
                }
                ImageStack tmp2Stack = tmp2.getStack();
                for (int z = 1; z <= slices2; z++) {
                    imp.setPositionWithoutUpdate(c, z, t);
                    int n2 = imp2.getStackIndex(c, z, t);
                    stack2.setPixels(tmp2Stack.getPixels(z), n2);
                }
            }
        }
        imp.setPosition(c1, z1, t1);
        if (channels > 1 && imp.isComposite()) {
            imp2 = new CompositeImage(imp2, ((CompositeImage) imp).getMode());
            ((CompositeImage) imp2).copyLuts(imp);
        }
        return imp2;
    }
