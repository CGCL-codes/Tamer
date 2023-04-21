    Image5D doI5DMontage(Image5D i5d, int columns, int rows, double scale, int first, int last, int inc, int borderWidth, boolean labels) {
        if (!i5d.lock()) return null;
        ImagePlus imp = (ImagePlus) i5d;
        int width = (int) (i5d.getWidth() * scale);
        int height = (int) (i5d.getHeight() * scale);
        int montageImageWidth = width * columns + borderWidth / 2;
        int montageImageHeight = height * rows + borderWidth / 2;
        int currentChannel = i5d.getCurrentChannel();
        int currentSlice = i5d.getCurrentSlice();
        int currentFrame = i5d.getCurrentFrame();
        int nMontagedChannels = 0;
        int[] montagedChannels = new int[i5d.getNChannels()];
        for (int c = 1; c <= i5d.getNChannels(); c++) {
            i5d.storeChannelProperties(c);
            if (bDisplayedChannelsOnly && ((i5d.getDisplayMode() == ChannelControl.OVERLAY && !i5d.getChannelDisplayProperties(c).isDisplayedInOverlay()) || i5d.getDisplayMode() != ChannelControl.OVERLAY && c != currentChannel)) continue;
            montagedChannels[nMontagedChannels] = c;
            nMontagedChannels++;
        }
        if (bDisplayedChannelsOnly && nMontagedChannels == 0) {
            return null;
        }
        int startFrame = i5d.getCurrentFrame();
        int nFrames = 1;
        if (bAllTimeFrames) {
            startFrame = 1;
            nFrames = i5d.getNFrames();
        }
        String newTitle = WindowManager.makeUniqueName(imp.getTitle() + " Montage");
        Image5D resultI5D = new Image5D(newTitle, i5d.getType(), montageImageWidth, montageImageHeight, nMontagedChannels, 1, nFrames, false);
        resultI5D.setCalibration(i5d.getCalibration().copy());
        for (int frame = startFrame; frame < startFrame + nFrames; frame++) {
            for (int destChannel = 1; destChannel <= nMontagedChannels; destChannel++) {
                int srcChannel = montagedChannels[destChannel - 1];
                i5d.setCurrentPosition(0, 0, srcChannel - 1, currentSlice - 1, frame - 1);
                ImagePlus tempImg = new ImagePlus(imp.getTitle() + " Montage", i5d.getStack());
                ImagePlus montage = makeMontage(tempImg, columns, rows, scale, first, last, inc, borderWidth, label);
                tempImg.flush();
                resultI5D.setPixels(montage.getProcessor().getPixels(), destChannel, 1, frame - startFrame + 1);
                montage.flush();
                if (frame == startFrame) {
                    if (destChannel == resultI5D.getCurrentChannel()) {
                        resultI5D.setChannelCalibration(destChannel, i5d.getChannelCalibration(srcChannel).copy());
                        resultI5D.setChannelDisplayProperties(destChannel, i5d.getChannelDisplayProperties(srcChannel).copy());
                        resultI5D.restoreCurrentChannelProperties();
                    } else {
                        resultI5D.setChannelCalibration(destChannel, i5d.getChannelCalibration(srcChannel).copy());
                        resultI5D.setChannelDisplayProperties(destChannel, i5d.getChannelDisplayProperties(srcChannel).copy());
                        resultI5D.restoreChannelProperties(destChannel);
                    }
                    if (!bDoScaling) {
                        resultI5D.getProcessor(destChannel).resetMinAndMax();
                    }
                }
            }
        }
        i5d.setCurrentPosition(0, 0, currentChannel - 1, currentSlice - 1, currentFrame - 1);
        i5d.unlock();
        return resultI5D;
    }
