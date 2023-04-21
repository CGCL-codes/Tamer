    private String readingToString(AP a) {
        return readingToString(a.getBestLat(), a.getBestLon(), a.getSsid(), "BBS", a.getMac(), a.getFirstSeen(), a.getMaxSnr(), a.getMaxSignal(), a.getMaxNoise(), a.getName(), a.getFlags(), a.getChannels(), a.getBeaconInterval());
    }
