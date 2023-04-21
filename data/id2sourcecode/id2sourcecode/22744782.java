    protected int[][] createCacheChannelMaps() {
        final int[][] fullChanMaps = fullScale.getChannelMaps();
        final int[][] cacheChanMaps = new int[fullChanMaps.length][];
        for (int i = 0; i < fullChanMaps.length; i++) {
            cacheChanMaps[i] = new int[fullChanMaps[i].length * modelChannels];
            for (int j = 0; j < cacheChanMaps[i].length; j++) {
                cacheChanMaps[i][j] = j;
            }
        }
        return cacheChanMaps;
    }
