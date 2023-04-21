    ImagePlus doProjection() {
        if (!imp.lock()) return null;
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
        byte[] reds = new byte[i5d.getWidth() * i5d.getHeight()];
        byte[] greens = new byte[i5d.getWidth() * i5d.getHeight()];
        byte[] blues = new byte[i5d.getWidth() * i5d.getHeight()];
        String newTitle = WindowManager.makeUniqueName(imp.getTitle() + " Projection");
        ImagePlus resultImp = IJ.createImage(newTitle, "rgb black", i5d.getWidth(), i5d.getHeight(), nFrames);
        resultImp.setCalibration(imp.getCalibration().copy());
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
                if (bDoScaling) {
                    proj.getProcessor().setMinAndMax(i5d.getChannelDisplayProperties(srcChannel).getMinValue(), i5d.getChannelDisplayProperties(srcChannel).getMaxValue());
                } else {
                    proj.getProcessor().resetMinAndMax();
                }
                ColorProcessor proc = (ColorProcessor) (new TypeConverter(proj.getProcessor(), bDoScaling)).convertToRGB();
                int[] rgb = new int[3];
                for (int x = 0; x < imp.getWidth(); x++) {
                    for (int y = 0; y < imp.getHeight(); y++) {
                        int pos = x + imp.getWidth() * y;
                        proc.getPixel(x, y, rgb);
                        int newval = rgb[0] + (0xff & reds[pos]);
                        if (newval < 256) {
                            reds[pos] = (byte) newval;
                        } else {
                            reds[pos] = (byte) 0xff;
                        }
                        newval = rgb[1] + (0xff & greens[pos]);
                        if (newval < 256) {
                            greens[pos] = (byte) newval;
                        } else {
                            greens[pos] = (byte) 0xff;
                        }
                        newval = rgb[2] + (0xff & blues[pos]);
                        if (newval < 256) {
                            blues[pos] = (byte) newval;
                        } else {
                            blues[pos] = (byte) 0xff;
                        }
                    }
                }
            }
            ColorProcessor cp = new ColorProcessor(imp.getWidth(), imp.getHeight());
            cp.setRGB(reds, greens, blues);
            resultImp.setSlice(frame - startFrame + 1);
            resultImp.setProcessor(null, cp);
            for (int i = 0; i < imp.getWidth() * imp.getHeight(); i++) {
                reds[i] = 0;
                greens[i] = 0;
                blues[i] = 0;
            }
        }
        i5d.setCurrentPosition(0, 0, currentChannel - 1, currentSlice - 1, currentFrame - 1);
        imp.unlock();
        return resultImp;
    }
