    public boolean encodeMidSide() {
        int[] lefts = channelFormat.getLeft();
        int[] rights = channelFormat.getRight();
        assert lefts.length == rights.length;
        if (lefts.length == 0) return false;
        int np = lefts.length;
        int ns = getSampleCount();
        for (int p = 0; p < np; p++) {
            float[] left = getChannel(lefts[p]);
            float[] right = getChannel(rights[p]);
            for (int s = 0; s < ns; s++) {
                float mid = 0.5f * (left[s] + right[s]);
                float side = 0.5f * (left[s] - right[s]);
                left[s] = mid;
                right[s] = side;
            }
        }
        return true;
    }
