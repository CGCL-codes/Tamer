    public void reduce(ImagePlus imp2) {
        int channels = imp2.getNChannels();
        int slices = imp2.getNSlices();
        int frames = imp2.getNFrames();
        int c1 = imp.getChannel();
        int z1 = imp.getSlice();
        int t1 = imp.getFrame();
        int i = 1;
        int n = channels * slices * frames;
        ImageStack stack = imp.getStack();
        ImageStack stack2 = imp2.getStack();
        for (int c = 1; c <= channels; c++) {
            if (channels == 1) c = c1;
            LUT lut = imp.isComposite() ? ((CompositeImage) imp).getChannelLut() : null;
            imp.setPositionWithoutUpdate(c, 1, 1);
            ImageProcessor ip = imp.getProcessor();
            double min = ip.getMin();
            double max = ip.getMax();
            for (int z = 1; z <= slices; z++) {
                if (slices == 1) z = z1;
                for (int t = 1; t <= frames; t++) {
                    if (frames == 1) t = t1;
                    imp.setPositionWithoutUpdate(c, z, t);
                    ip = imp.getProcessor();
                    int n1 = imp.getStackIndex(c, z, t);
                    String label = stack.getSliceLabel(n1);
                    int n2 = imp2.getStackIndex(c, z, t);
                    if (stack2.getPixels(n2) != null) stack2.getProcessor(n2).insert(ip, 0, 0); else stack2.setPixels(ip.getPixels(), n2);
                    stack2.setSliceLabel(label, n2);
                }
            }
            if (lut != null) {
                if (imp2.isComposite()) ((CompositeImage) imp2).setChannelLut(lut); else imp2.getProcessor().setColorModel(lut);
            }
            imp2.getProcessor().setMinAndMax(min, max);
        }
        imp.setPosition(c1, z1, t1);
        imp2.resetStack();
        imp2.setPosition(1, 1, 1);
    }
