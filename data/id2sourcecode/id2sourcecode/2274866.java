    void saveDisplayRangesAndLuts(ImagePlus imp, FileInfo fi) {
        CompositeImage ci = (CompositeImage) imp;
        int channels = imp.getNChannels();
        fi.displayRanges = new double[channels * 2];
        for (int i = 1; i <= channels; i++) {
            LUT lut = ci.getChannelLut(i);
            fi.displayRanges[(i - 1) * 2] = lut.min;
            fi.displayRanges[(i - 1) * 2 + 1] = lut.max;
        }
        if (ci.hasCustomLuts()) {
            fi.channelLuts = new byte[channels][];
            for (int i = 0; i < channels; i++) {
                LUT lut = ci.getChannelLut(i + 1);
                byte[] bytes = lut.getBytes();
                if (bytes == null) {
                    fi.channelLuts = null;
                    break;
                }
                fi.channelLuts[i] = bytes;
            }
        }
    }
