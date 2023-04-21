    void setupNewImage(ImagePlus imp, ImageProcessor ip) {
        Undo.reset();
        previousMin = min;
        previousMax = max;
        if (RGBImage) {
            ip.snapshot();
            previousSnapshot = ((ColorProcessor) ip).getSnapshotPixels();
        } else previousSnapshot = null;
        double min2 = imp.getDisplayRangeMin();
        double max2 = imp.getDisplayRangeMax();
        if (imp.getType() == ImagePlus.COLOR_RGB) {
            min2 = 0.0;
            max2 = 255.0;
        }
        if ((ip instanceof ShortProcessor) || (ip instanceof FloatProcessor)) {
            imp.resetDisplayRange();
            defaultMin = imp.getDisplayRangeMin();
            defaultMax = imp.getDisplayRangeMax();
        } else {
            defaultMin = 0;
            defaultMax = 255;
        }
        setMinAndMax(imp, min2, max2);
        min = imp.getDisplayRangeMin();
        max = imp.getDisplayRangeMax();
        if (IJ.debugMode) {
            IJ.log("min: " + min);
            IJ.log("max: " + max);
            IJ.log("defaultMin: " + defaultMin);
            IJ.log("defaultMax: " + defaultMax);
        }
        plot.defaultMin = defaultMin;
        plot.defaultMax = defaultMax;
        int valueRange = (int) (defaultMax - defaultMin);
        int newSliderRange = valueRange;
        if (newSliderRange > 640 && newSliderRange < 1280) newSliderRange /= 2; else if (newSliderRange >= 1280) newSliderRange /= 5;
        if (newSliderRange < 256) newSliderRange = 256;
        if (newSliderRange > 1024) newSliderRange = 1024;
        double displayRange = max - min;
        if (valueRange >= 1280 && valueRange != 0 && displayRange / valueRange < 0.25) newSliderRange *= 1.6666;
        if (newSliderRange != sliderRange) {
            sliderRange = newSliderRange;
            updateScrollBars(null, true);
        } else updateScrollBars(null, false);
        if (balance) {
            if (imp.isComposite()) {
                int channel = imp.getChannel();
                if (channel <= 4) {
                    choice.select(channel - 1);
                    channels = channelConstants[channel - 1];
                }
                if (choice.getItem(0).equals("Red")) {
                    choice.removeAll();
                    addBalanceChoices();
                }
            } else {
                if (choice.getItem(0).equals("Channel 1")) {
                    choice.removeAll();
                    addBalanceChoices();
                }
            }
        }
        if (!doReset) plotHistogram(imp);
        autoThreshold = 0;
        if (imp.isComposite()) IJ.setKeyUp(KeyEvent.VK_SHIFT);
    }
