    public synchronized void updateImage() {
        int imageSize = width * height;
        int nChannels = getNChannels();
        int redValue, greenValue, blueValue;
        int ch = getChannel();
        if (ch > nChannels) ch = nChannels;
        boolean newChannel = false;
        if (ch - 1 != currentChannel) {
            previousChannel = currentChannel;
            currentChannel = ch - 1;
            newChannel = true;
        }
        ImageProcessor ip = getProcessor();
        if (mode != COMPOSITE) {
            if (newChannel) {
                setupLuts(nChannels);
                LUT cm = lut[currentChannel];
                if (mode == COLOR) ip.setColorModel(cm);
                if (!(cm.min == 0.0 && cm.max == 0.0)) ip.setMinAndMax(cm.min, cm.max);
                if (!IJ.isMacro()) ContrastAdjuster.update();
                Frame channels = Channels.getInstance();
                for (int i = 0; i < MAX_CHANNELS; i++) active[i] = i == currentChannel ? true : false;
                if (channels != null) ((Channels) channels).update();
            }
            img = ip.createImage();
            return;
        }
        if (nChannels == 1) {
            cip = null;
            rgbPixels = null;
            awtImage = null;
            if (ip != null) img = ip.createImage();
            return;
        }
        if (cip == null || cip[0].getWidth() != width || cip[0].getHeight() != height || getBitDepth() != bitDepth) {
            setup(nChannels, getImageStack());
            rgbPixels = null;
            rgbSampleModel = null;
            if (currentChannel >= nChannels) {
                setSlice(1);
                currentChannel = 0;
                newChannel = true;
            }
            bitDepth = getBitDepth();
        }
        if (newChannel) {
            getProcessor().setMinAndMax(cip[currentChannel].getMin(), cip[currentChannel].getMax());
            if (!IJ.isMacro()) ContrastAdjuster.update();
        }
        if (getSlice() != currentSlice || getFrame() != currentFrame || channelsUpdated) {
            channelsUpdated = false;
            currentSlice = getSlice();
            currentFrame = getFrame();
            int position = getStackIndex(1, currentSlice, currentFrame);
            if (cip == null) return;
            for (int i = 0; i < nChannels; ++i) cip[i].setPixels(getImageStack().getProcessor(position + i).getPixels());
        }
        if (rgbPixels == null) {
            rgbPixels = new int[imageSize];
            newPixels = true;
            imageSource = null;
            rgbRaster = null;
            rgbImage = null;
        }
        cip[currentChannel].setMinAndMax(ip.getMin(), ip.getMax());
        if (singleChannel && nChannels <= 3) {
            switch(currentChannel) {
                case 0:
                    cip[0].updateComposite(rgbPixels, 1);
                    break;
                case 1:
                    cip[1].updateComposite(rgbPixels, 2);
                    break;
                case 2:
                    cip[2].updateComposite(rgbPixels, 3);
                    break;
            }
        } else {
            if (cip == null) return;
            if (syncChannels) {
                ImageProcessor ip2 = getProcessor();
                double min = ip2.getMin(), max = ip2.getMax();
                for (int i = 0; i < nChannels; i++) {
                    cip[i].setMinAndMax(min, max);
                    lut[i].min = min;
                    lut[i].max = max;
                }
                syncChannels = false;
            }
            if (active[0]) cip[0].updateComposite(rgbPixels, 4); else {
                for (int i = 1; i < imageSize; i++) rgbPixels[i] = 0;
            }
            if (cip == null) return;
            for (int i = 1; i < nChannels; i++) {
                if (active[i]) cip[i].updateComposite(rgbPixels, 5);
            }
        }
        if (IJ.isJava16()) createBufferedImage(); else createImage();
        if (img == null && awtImage != null) img = awtImage;
        singleChannel = false;
    }
