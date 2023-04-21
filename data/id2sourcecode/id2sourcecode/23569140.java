    void setMinAndMax(ImagePlus imp, ImageProcessor ip) {
        min = imp.getDisplayRangeMin();
        max = imp.getDisplayRangeMax();
        Calibration cal = imp.getCalibration();
        int digits = (ip instanceof FloatProcessor) || cal.calibrated() ? 2 : 0;
        double minValue = cal.getCValue(min);
        double maxValue = cal.getCValue(max);
        int channels = imp.getNChannels();
        GenericDialog gd = new GenericDialog("Set Display Range");
        gd.addNumericField("Minimum displayed value: ", minValue, digits);
        gd.addNumericField("Maximum displayed value: ", maxValue, digits);
        gd.addChoice("Unsigned 16-bit range:", ranges, ranges[getRangeIndex()]);
        gd.addCheckbox("Propagate to all open images", false);
        if (imp.isComposite()) gd.addCheckbox("Propagate to all " + channels + " channels", false);
        gd.showDialog();
        if (gd.wasCanceled()) return;
        minValue = gd.getNextNumber();
        maxValue = gd.getNextNumber();
        minValue = cal.getRawValue(minValue);
        maxValue = cal.getRawValue(maxValue);
        int rangeIndex = gd.getNextChoiceIndex();
        int range1 = ImagePlus.getDefault16bitRange();
        int range2 = setRange(rangeIndex);
        if (range1 != range2 && imp.getType() == ImagePlus.GRAY16 && !cal.isSigned16Bit()) {
            reset(imp, ip);
            minValue = imp.getDisplayRangeMin();
            maxValue = imp.getDisplayRangeMax();
        }
        boolean propagate = gd.getNextBoolean();
        boolean allChannels = imp.isComposite() && gd.getNextBoolean();
        if (maxValue >= minValue) {
            min = minValue;
            max = maxValue;
            setMinAndMax(imp, min, max);
            updateScrollBars(null, false);
            if (RGBImage) doMasking(imp, ip);
            if (propagate) IJ.runMacroFile("ij.jar:PropagateMinAndMax");
            if (allChannels) {
                int channel = imp.getChannel();
                for (int c = 1; c <= channels; c++) {
                    imp.setPositionWithoutUpdate(c, imp.getSlice(), imp.getFrame());
                    imp.setDisplayRange(min, max);
                }
                ((CompositeImage) imp).reset();
                imp.setPosition(channel, imp.getSlice(), imp.getFrame());
            }
            if (Recorder.record) {
                if (imp.getBitDepth() == 32) recordSetMinAndMax(min, max); else {
                    int imin = (int) min;
                    int imax = (int) max;
                    if (cal.isSigned16Bit()) {
                        imin = (int) cal.getCValue(imin);
                        imax = (int) cal.getCValue(imax);
                    }
                    recordSetMinAndMax(imin, imax);
                }
                if (Recorder.scriptMode()) Recorder.recordCall("ImagePlus.setDefault16bitRange(" + range2 + ");"); else Recorder.recordString("call(\"ij.ImagePlus.setDefault16bitRange\", " + range2 + ");\n");
            }
        }
    }
