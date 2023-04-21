    private String readingToString(AP a, Encounter e) {
        int signal = e.getSignal() + 149;
        int noise = e.getNoise() + 149;
        int snr = signal - noise;
        return readingToString(e.getLat(), e.getLon(), a.getSsid(), "BBS", a.getMac(), e.getTime(), snr, signal, noise, a.getName(), a.getFlags(), a.getChannels(), a.getBeaconInterval());
    }
