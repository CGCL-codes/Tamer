    void invertLut() {
        ImagePlus imp = WindowManager.getCurrentImage();
        if (imp == null) {
            IJ.noImage();
            return;
        }
        if (imp.getType() == ImagePlus.COLOR_RGB) {
            IJ.error("RGB images do not use LUTs");
            return;
        }
        if (imp.isComposite()) {
            CompositeImage ci = (CompositeImage) imp;
            LUT lut = ci.getChannelLut();
            if (lut != null) ci.setChannelLut(lut.createInvertedLut());
        } else {
            ImageProcessor ip = imp.getProcessor();
            ip.invertLut();
            if (imp.getStackSize() > 1) imp.getStack().setColorModel(ip.getColorModel());
        }
        imp.updateAndRepaintWindow();
    }
