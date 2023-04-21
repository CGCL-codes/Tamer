    public Article memberUpdate(Article bean, CmsMember cmsMember, UploadRule rule) {
        Assert.notNull(bean);
        Assert.notNull(cmsMember);
        Assert.notNull(rule);
        Article entity = findById(bean.getId());
        entity.setCheckStep(-1);
        entity.setReject(false);
        entity.calculatePageCount();
        int origCount = entity.getPageCount();
        CmsChannel origChnl = entity.getChannel();
        updateByUpdater(createMemberUpdate(bean));
        entity.writeContent(contextPvd.getAppRoot(), origCount);
        CmsChannel currChnl = entity.getChannel();
        if (!currChnl.equals(origChnl)) {
            currChnl.setDocCount(currChnl.getDocCount() + 1);
            origChnl.setDocCount(origChnl.getDocCount() - 1);
        }
        removeAttachment(entity, false);
        addAttachment(entity, rule, cmsMember.getMember().getUser(), null);
        return entity;
    }
