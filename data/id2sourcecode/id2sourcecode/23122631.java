        void translate(int[] x) {
            final int aslice = imp.getCurrentSlice() - 1 + x[slice];
            final int aframe = imp.getFrame() + x[frame];
            final int achannel = imp.getChannel() + x[channel];
            imp.setPositionWithoutUpdate(achannel, aframe, aslice);
        }
