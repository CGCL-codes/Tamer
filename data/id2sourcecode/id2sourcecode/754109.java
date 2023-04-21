    Image5D doI5DProjection() {
        int currentChannel = i5d.getCurrentChannel();
        int currentSlice = i5d.getCurrentSlice();
        int currentFrame = i5d.getCurrentFrame();
        int nProjectedChannels = 0;
        int[] projectedChannels = new int[i5d.getNChannels()];
        for (int c = 1; c <= i5d.getNChannels(); c++) {
            i5d.storeChannelProperties(c);
            if (bDisplayedChannelsOnly && ((i5d.getDisplayMode() == ChannelControl.OVERLAY && !i5d.getChannelDisplayProperties(c).isDisplayedInOverlay()) || i5d.getDisplayMode() != ChannelControl.OVERLAY && c != currentChannel)) continue;
            projectedChannels[nProjectedChannels] = c;
            nProjectedChannels++;
        }
        if (bDisplayedChannelsOnly && nProjectedChannels == 0) {
            return null;
        }
        int startFrame = i5d.getCurrentFrame();
        int nFrames = 1;
        if (bAllTimeFrames) {
            startFrame = 1;
            nFrames = i5d.getNFrames();
        }
        String newTitle = WindowManager.makeUniqueName(imp.getTitle() + " Projection");
        Image5D resultI5D = new Image5D(newTitle, i5d.getType(), i5d.getWidth(), i5d.getHeight(), nProjectedChannels, 1, nFrames, false);
        resultI5D.setCalibration(i5d.getCalibration().copy());
        for (int frame = startFrame; frame < startFrame + nFrames; frame++) {
            for (int destChannel = 1; destChannel <= nProjectedChannels; destChannel++) {
                int srcChannel = projectedChannels[destChannel - 1];
                i5d.setCurrentPosition(0, 0, srcChannel - 1, currentSlice - 1, frame - 1);
                ImagePlus tempImg = new ImagePlus(imp.getTitle() + " Projection", i5d.getStack());
                ZProjector zp = new ZProjector(tempImg);
                zp.setStartSlice(startSlice);
                zp.setStopSlice(stopSlice);
                zp.setMethod(method);
                zp.doProjection();
                ImagePlus proj = zp.getProjection();
                resultI5D.setPixels(proj.getProcessor().getPixels(), destChannel, 1, frame - startFrame + 1);
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
        imp.unlock();
        return resultI5D;
    }
