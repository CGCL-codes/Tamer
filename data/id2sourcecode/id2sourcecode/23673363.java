    public void run(String arg) {
        ImagePlus imp = WindowManager.getCurrentImage();
        if (imp == null || imp.getStackSize() == 1) {
            IJ.error("Stack required");
            return;
        }
        if (imp.isHyperStack()) {
            IJ.error("Make Montage", "Make Montage does not work directly with hyperstacks. You need\n" + "to first use the Image>Type>RGB Color command to create a Z or T\n" + "series, or use Image>Hyperstacks>Reduce Dimensionality to create\n" + "a stack that can be used to create a channel montage.");
            return;
        }
        int channels = imp.getNChannels();
        if (imp.isComposite() && channels > 1) {
            int channel = imp.getChannel();
            CompositeImage ci = (CompositeImage) imp;
            int mode = ci.getMode();
            if (mode == CompositeImage.COMPOSITE) ci.setMode(CompositeImage.COLOR);
            ImageStack stack = new ImageStack(imp.getWidth(), imp.getHeight());
            for (int c = 1; c <= channels; c++) {
                imp.setPositionWithoutUpdate(c, imp.getSlice(), imp.getFrame());
                Image img = imp.getImage();
                stack.addSlice(null, new ColorProcessor(img));
            }
            if (ci.getMode() != mode) ci.setMode(mode);
            imp.setPosition(channel, imp.getSlice(), imp.getFrame());
            imp = new ImagePlus(imp.getTitle(), stack);
        }
        makeMontage(imp);
        imp.updateImage();
        saveID = imp.getID();
        IJ.register(MontageMaker.class);
    }
