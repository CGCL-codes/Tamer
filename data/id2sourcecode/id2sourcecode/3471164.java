    void duplicateHyperstack(ImagePlus imp, String newTitle) {
        newTitle = showHSDialog(imp, newTitle);
        if (newTitle == null) return;
        ImagePlus imp2 = null;
        Roi roi = imp.getRoi();
        if (!duplicateStack) {
            int nChannels = imp.getNChannels();
            boolean singleComposite = imp.isComposite() && nChannels == imp.getStackSize();
            if (!singleComposite && nChannels > 1 && imp.isComposite() && ((CompositeImage) imp).getMode() == CompositeImage.COMPOSITE) {
                firstC = 1;
                lastC = nChannels;
            } else firstC = lastC = imp.getChannel();
            firstZ = lastZ = imp.getSlice();
            firstT = lastT = imp.getFrame();
        }
        imp2 = run(imp, firstC, lastC, firstZ, lastZ, firstT, lastT);
        if (imp2 == null) return;
        Calibration cal = imp2.getCalibration();
        if (roi != null && (cal.xOrigin != 0.0 || cal.yOrigin != 0.0)) {
            cal.xOrigin -= roi.getBounds().x;
            cal.yOrigin -= roi.getBounds().y;
        }
        imp2.setTitle(newTitle);
        imp2.show();
        if (roi != null && roi.isArea() && roi.getType() != Roi.RECTANGLE) imp2.restoreRoi();
        if (IJ.isMacro() && imp2.getWindow() != null) IJ.wait(50);
    }
