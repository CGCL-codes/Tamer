    private static int[][] createChannelMaps(InterleavedStreamFile[] fs) {
        final int[][] channelMaps = new int[fs.length][];
        int[] channelMap;
        for (int i = 0; i < fs.length; i++) {
            channelMap = new int[fs[i].getChannelNum()];
            for (int j = 0; j < channelMap.length; j++) {
                channelMap[j] = j;
            }
            channelMaps[i] = channelMap;
        }
        return channelMaps;
    }
