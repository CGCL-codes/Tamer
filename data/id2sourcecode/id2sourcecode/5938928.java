    public Download updateDownload(Download bean, CmsAdmin admin, CmsMember member, UploadRule rule, String resUrl, String downloadAttch, long topTime) {
        Assert.notNull(bean);
        Assert.notNull(rule);
        Assert.notNull(resUrl);
        CmsChannel origChnl = bean.getChannel();
        Download d = findById(bean.getId());
        updateByUpdater(createUpdater(bean));
        CmsChannel currChnl = d.getChannel();
        if (!currChnl.equals(origChnl)) {
            currChnl.setDocCount(currChnl.getDocCount() + 1);
            origChnl.setDocCount(origChnl.getDocCount() - 1);
        }
        removeAttachment(d, false);
        addAttachment(d, rule, admin, downloadAttch, member);
        return bean;
    }
