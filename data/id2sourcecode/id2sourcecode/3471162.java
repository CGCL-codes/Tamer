    public ImagePlus run(ImagePlus imp, int firstC, int lastC, int firstZ, int lastZ, int firstT, int lastT) {
        Rectangle rect = null;
        Roi roi = imp.getRoi();
        if (roi != null && roi.isArea()) rect = roi.getBounds();
        int width = rect != null ? rect.width : imp.getWidth();
        int height = rect != null ? rect.height : imp.getHeight();
        ImageStack stack = imp.getStack();
        ImageStack stack2 = new ImageStack(width, height);
        for (int t = firstT; t <= lastT; t++) {
            for (int z = firstZ; z <= lastZ; z++) {
                for (int c = firstC; c <= lastC; c++) {
                    int n1 = imp.getStackIndex(c, z, t);
                    ImageProcessor ip = stack.getProcessor(n1);
                    String label = stack.getSliceLabel(n1);
                    ip.setRoi(rect);
                    ip = ip.crop();
                    stack2.addSlice(label, ip);
                }
            }
        }
        ImagePlus imp2 = imp.createImagePlus();
        imp2.setStack("DUP_" + imp.getTitle(), stack2);
        imp2.setDimensions(lastC - firstC + 1, lastZ - firstZ + 1, lastT - firstT + 1);
        if (imp.isComposite()) {
            int mode = ((CompositeImage) imp).getMode();
            if (lastC > firstC) {
                imp2 = new CompositeImage(imp2, mode);
                int i2 = 1;
                for (int i = firstC; i <= lastC; i++) {
                    LUT lut = ((CompositeImage) imp).getChannelLut(i);
                    ((CompositeImage) imp2).setChannelLut(lut, i2++);
                }
            } else if (firstC == lastC) {
                LUT lut = ((CompositeImage) imp).getChannelLut(firstC);
                imp2.getProcessor().setColorModel(lut);
                imp2.setDisplayRange(lut.min, lut.max);
            }
        }
        imp2.setOpenAsHyperStack(true);
        if (Recorder.record) Recorder.recordCall("imp = new Duplicator().run(imp, " + firstC + ", " + lastC + ", " + firstZ + ", " + lastZ + ", " + firstT + ", " + lastT + ");");
        return imp2;
    }
