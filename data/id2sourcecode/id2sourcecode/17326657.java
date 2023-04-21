    public List getChannels() {
        if (fRssChannels.size() > 0) {
            return fRssChannels;
        } else {
            fRssChannels.add(fCurrentChannel);
            return fRssChannels;
        }
    }
