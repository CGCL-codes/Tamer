    public void addTochannels(com.jeecms.cms.entity.CmsChannel cmsChannel) {
        if (null == getChannels()) setChannels(new java.util.TreeSet<com.jeecms.cms.entity.CmsChannel>());
        getChannels().add(cmsChannel);
    }
