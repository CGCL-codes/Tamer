    public Article saveArticle(Article bean, CmsAdmin admin, UploadRule rule, String resUrl, int checkCount, long topTime) {
        Assert.notNull(bean);
        Assert.notNull(admin);
        Assert.notNull(rule);
        Assert.notNull(resUrl);
        initDefValue(bean);
        handleTitleImg(bean);
        handleDate(bean, topTime);
        handleCheckRight(bean, admin, checkCount);
        bean.calculatePageCount();
        bean.setContentResPath(resUrl);
        bean.setAdminInput(admin);
        bean = save(bean);
        bean.writeContent(contextPvd.getAppRoot(), 0);
        CmsChannel chnl = bean.getChannel();
        chnl.setDocCount(chnl.getDocCount() + 1);
        addSideArticle(bean);
        addAttachment(bean, rule, admin.getAdmin().getUser(), null);
        return bean;
    }
