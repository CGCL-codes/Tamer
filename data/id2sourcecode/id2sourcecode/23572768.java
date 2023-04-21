    public Article memberSave(Article bean, CmsMember member, UploadRule rule) {
        Assert.notNull(bean);
        Assert.notNull(member);
        Assert.notNull(rule);
        Website web = bean.getWebsite();
        ContentCtg ctg = contentCtgMng.getFirstCtg(web.getRootWebId());
        bean.setContentCtg(ctg);
        bean.setConfig(cmsConfigMng.findById(web.getId()));
        initDefValue(bean);
        handleDate(bean, 0);
        bean.calculatePageCount();
        bean.setContentResPath(web.getResUrl());
        bean.setAdminInput(null);
        bean.setMember(member);
        bean = save(bean);
        bean.writeContent(contextPvd.getAppRoot(), 0);
        CmsChannel chnl = bean.getChannel();
        chnl.setDocCount(chnl.getDocCount() + 1);
        addAttachment(bean, rule, member.getMember().getUser(), member);
        return bean;
    }
