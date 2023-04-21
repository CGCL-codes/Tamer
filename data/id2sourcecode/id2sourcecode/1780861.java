    public boolean decodeMidSide() {
        int[] mids = channelFormat.getLeft();
        int[] sides = channelFormat.getRight();
        assert mids.length == sides.length;
        if (mids.length == 0) return false;
        int np = mids.length;
        int ns = getSampleCount();
        for (int p = 0; p < np; p++) {
            float[] mid = getChannel(mids[p]);
            float[] side = getChannel(sides[p]);
            for (int s = 0; s < ns; s++) {
                float left = mid[s] + side[s];
                float right = mid[s] - side[s];
                mid[s] = left;
                side[s] = right;
            }
        }
        return true;
    }
