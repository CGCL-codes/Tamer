    public void convertHyperstack(ImagePlus imp, ImagePlus imp2) {
        int slices = imp2.getNSlices();
        int frames = imp2.getNFrames();
        int c1 = imp.getChannel();
        int z1 = imp.getSlice();
        int t1 = imp.getFrame();
        int i = 1;
        int c = 1;
        ImageStack stack = imp.getStack();
        ImageStack stack2 = imp2.getStack();
        imp.setPositionWithoutUpdate(c, 1, 1);
        ImageProcessor ip = imp.getProcessor();
        double min = ip.getMin();
        double max = ip.getMax();
        for (int z = 1; z <= slices; z++) {
            if (slices == 1) z = z1;
            for (int t = 1; t <= frames; t++) {
                if (frames == 1) t = t1;
                imp.setPositionWithoutUpdate(c, z, t);
                Image img = imp.getImage();
                int n2 = imp2.getStackIndex(c, z, t);
                stack2.setPixels((new ColorProcessor(img)).getPixels(), n2);
            }
        }
        imp.setPosition(c1, z1, t1);
        imp2.resetStack();
        imp2.setPosition(1, 1, 1);
    }
