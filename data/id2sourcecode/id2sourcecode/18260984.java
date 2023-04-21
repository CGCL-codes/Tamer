    public void addTochannels(CmsChannel chnl) {
        Set<CmsChannel> set = getChannels();
        if (set == null) {
            set = new HashSet<CmsChannel>();
            setChannels(set);
        }
        chnl.addToAdmins(this);
        set.add(chnl);
    }
