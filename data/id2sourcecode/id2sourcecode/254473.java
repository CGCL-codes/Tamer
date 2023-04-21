    ImagePlus doMontage(Image5D i5d, int columns, int rows, double scale, int first, int last, int inc, int borderWidth, boolean labels) {
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
        byte[] reds = new byte[montageImageWidth * montageImageHeight];
        byte[] greens = new byte[montageImageWidth * montageImageHeight];
        byte[] blues = new byte[montageImageWidth * montageImageHeight];
        String newTitle = WindowManager.makeUniqueName(imp.getTitle() + " Montage");
        ImagePlus resultImp = IJ.createImage(newTitle, "rgb black", montageImageWidth, montageImageHeight, nFrames);
        resultImp.setCalibration(imp.getCalibration().copy());
        for (int frame = startFrame; frame < startFrame + nFrames; frame++) {
            for (int destChannel = 1; destChannel <= nMontagedChannels; destChannel++) {
                int srcChannel = montagedChannels[destChannel - 1];
                i5d.setCurrentPosition(0, 0, srcChannel - 1, currentSlice - 1, frame - 1);
                ImageStack tmp = i5d.getStack();
                ImagePlus tempImg = new ImagePlus(imp.getTitle() + " Montage", tmp);
                ImagePlus montage = makeMontage(tempImg, columns, rows, scale, first, last, inc, borderWidth, label);
                tempImg.flush();
                if (bDoScaling) {
                    montage.getProcessor().setMinAndMax(i5d.getChannelDisplayProperties(srcChannel).getMinValue(), i5d.getChannelDisplayProperties(srcChannel).getMaxValue());
                } else {
                    montage.getProcessor().resetMinAndMax();
                }
                ColorProcessor proc = (ColorProcessor) (new TypeConverter(montage.getProcessor(), bDoScaling)).convertToRGB();
                int[] rgb = new int[3];
                for (int x = 0; x < montageImageWidth; x++) {
                    for (int y = 0; y < montageImageHeight; y++) {
                        int pos = x + montageImageWidth * y;
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
                montage.flush();
            }
            ColorProcessor cp = new ColorProcessor(montageImageWidth, montageImageHeight);
            cp.setRGB(reds, greens, blues);
            resultImp.setSlice(frame - startFrame + 1);
            resultImp.setProcessor(null, cp);
            for (int i = 0; i < montageImageWidth * montageImageHeight; i++) {
                reds[i] = 0;
                greens[i] = 0;
                blues[i] = 0;
            }
        }
        i5d.setCurrentPosition(0, 0, currentChannel - 1, currentSlice - 1, currentFrame - 1);
        i5d.unlock();
        return resultImp;
    }
