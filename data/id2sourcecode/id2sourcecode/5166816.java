    public void setChannelLut(LUT table) {
        if (mode == GRAYSCALE) getProcessor().setColorModel(table); else {
            int c = getChannelIndex();
            double min = lut[c].min;
            double max = lut[c].max;
            lut[c] = table;
            lut[c].min = min;
            lut[c].max = max;
            if (mode == COMPOSITE && cip != null && c < cip.length) {
                cip[c].setColorModel(lut[c]);
                imageSource = null;
                newPixels = true;
                img = null;
            }
            currentChannel = -1;
            if (!IJ.isMacro()) ContrastAdjuster.update();
        }
        customLuts = true;
    }
