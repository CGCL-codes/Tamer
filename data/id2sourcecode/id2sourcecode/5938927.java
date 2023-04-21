    public Download saveDownload(Download bean, CmsAdmin admin, CmsMember member, UploadRule rule, String resUrl, String downloadAttch, long topTime) {
        Assert.notNull(bean);
        Assert.notNull(rule);
        Assert.notNull(resUrl);
        bean.setContentResPath(resUrl);
        initDefValue(bean);
        handleDate(bean);
        bean = save(bean);
        CmsChannel chnl = bean.getChannel();
        chnl.setDocCount(chnl.getDocCount() + 1);
        addAttachment(bean, rule, admin, downloadAttch, member);
        return bean;
    }
