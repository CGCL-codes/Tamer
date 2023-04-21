    public Article updateArticle(Article arti, CmsAdmin admin, UploadRule rule, long topTime) {
        Assert.notNull(arti);
        Assert.notNull(admin);
        Assert.notNull(rule);
        Article entity = findById(arti.getId());
        Website web = entity.getWebsite();
        User user = admin.getAdmin().getUser();
        admin = cmsAdminMng.getAdminByUserId(web.getId(), user.getId());
        handleTitleImg(arti);
        arti.calculatePageCount();
        int origCount = entity.getPageCount();
        CmsChannel origChnl = entity.getChannel();
        boolean origCheck = entity.getCheck();
        updateByUpdater(createUpdater(arti));
        entity.writeContent(contextPvd.getAppRoot(), origCount);
        handleTopTimeForUpdate(entity, topTime);
        handleCheckRight(entity, admin, entity.getConfig().getCheckCount());
        if (entity.getAdminInput() == null) {
            entity.setAdminInput(admin);
        }
        boolean currCheck = entity.getCheck();
        if (currCheck != origCheck) {
            if (currCheck) {
                addSideArticle(entity);
            } else {
                removeSideArticle(entity);
            }
        }
        CmsChannel currChnl = entity.getChannel();
        if (!currChnl.equals(origChnl)) {
            currChnl.setDocCount(currChnl.getDocCount() + 1);
            origChnl.setDocCount(origChnl.getDocCount() - 1);
            removeSideArticle(entity);
            addSideArticle(entity);
        }
        removeAttachment(entity, false);
        addAttachment(entity, rule, user, null);
        return entity;
    }
