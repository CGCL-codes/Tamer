    public void updateImage() {
        int imageSize = width * height;
        int nChannels = getNChannels();
        int redValue, greenValue, blueValue;
        ImageProcessor tmpProcessor = ip;
        int type = getType();
        int displayMode = -1;
        if (win != null) displayMode = ((Image5DWindow) win).getDisplayMode();
        if ((displayMode == ChannelControl.ONE_CHANNEL_GRAY) || (displayMode == ChannelControl.ONE_CHANNEL_COLOR)) {
            img = ip.createImage();
        } else if (displayMode == ChannelControl.OVERLAY || displayMode == ChannelControl.TILED) {
            if (awtImagePixels == null || awtImagePixels.length != imageSize) {
                awtImagePixels = new int[imageSize];
                newPixels = true;
            }
            if (awtChannelPixels == null || awtChannelPixels.length != nChannels || awtChannelPixels[0].length != imageSize) {
                awtChannelPixels = new int[nChannels][];
                for (int i = 0; i < nChannels; ++i) {
                    awtChannelPixels[i] = new int[imageSize];
                }
            }
            if (displayMode == ChannelControl.TILED) {
                switch(type) {
                    case GRAY8:
                        tmpProcessor = new ByteProcessor(width, height, null, null);
                        break;
                    case GRAY16:
                        dummyImage = new short[imageSize];
                        tmpProcessor = new ShortProcessor(width, height, null, null);
                        break;
                    case GRAY32:
                        tmpProcessor = new FloatProcessor(width, height, null, null);
                        break;
                }
                channelIPs[getCurrentChannel() - 1].createImage();
            }
            for (int i = 0; i < nChannels; ++i) {
                if (!chDisplayProps[i].isDisplayedInOverlay()) continue;
                if (displayMode == ChannelControl.OVERLAY) {
                    PixelGrabber pg = new PixelGrabber(channelIPs[i].createImage(), 0, 0, width, height, awtChannelPixels[i], 0, width);
                    try {
                        pg.grabPixels();
                    } catch (InterruptedException e) {
                    }
                    ;
                } else if (displayMode == ChannelControl.TILED) {
                    tmpProcessor.setPixels(channelIPs[i].getPixels());
                    tmpProcessor.setColorModel(getChannelDisplayProperties(i + 1).getColorModel());
                    tmpProcessor.setMinAndMax(channelIPs[i].getMin(), channelIPs[i].getMax());
                    PixelGrabber pg = new PixelGrabber(tmpProcessor.createImage(), 0, 0, width, height, awtChannelPixels[i], 0, width);
                    try {
                        pg.grabPixels();
                    } catch (InterruptedException e) {
                    }
                    ;
                }
            }
            for (int i = 0; i < imageSize; ++i) {
                redValue = 0;
                greenValue = 0;
                blueValue = 0;
                for (int j = 0; j < nChannels; ++j) {
                    if (!chDisplayProps[j].isDisplayedInOverlay()) continue;
                    redValue += (awtChannelPixels[j][i] >> 16) & 0xFF;
                    greenValue += (awtChannelPixels[j][i] >> 8) & 0xFF;
                    blueValue += (awtChannelPixels[j][i]) & 0xFF;
                    if (redValue > 255) redValue = 255;
                    if (greenValue > 255) greenValue = 255;
                    if (blueValue > 255) blueValue = 255;
                }
                awtImagePixels[i] = (redValue << 16) | (greenValue << 8) | (blueValue);
            }
            if (img == null && awtImage != null) img = awtImage;
            if (imageSource == null) {
                imageColorModel = new DirectColorModel(32, 0xFF0000, 0xFF00, 0xFF);
                imageSource = new MemoryImageSource(width, height, imageColorModel, awtImagePixels, 0, width);
                imageSource.setAnimated(true);
                imageSource.setFullBufferUpdates(true);
                awtImage = Toolkit.getDefaultToolkit().createImage(imageSource);
                newPixels = false;
            } else if (newPixels) {
                imageSource.newPixels(awtImagePixels, imageColorModel, 0, width);
                newPixels = false;
            } else {
                imageSource.newPixels();
            }
        }
    }
