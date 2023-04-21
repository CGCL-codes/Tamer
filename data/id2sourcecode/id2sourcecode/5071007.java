    @SuppressWarnings("unchecked")
    public String edit() {
        bean = articleMng.findById(id);
        itemMap = bean.getChannel().getModel().getDiplayItemMap(ChnlModel.CONTENT_ITEM);
        Long webId = bean.getWebsite().getRootWebId();
        contentCtgList = contentCtgMng.getList(webId, false);
        memberGroupList = cmsMemberGroupMng.getList(webId, 0, true);
        tplContentList = bean.getChannel().getModel().tplContentList(bean.getConfig(), ARTICLE_SYS, contextPvd.getAppRoot());
        List<CmsChannel> chnlList = cmsChannelMng.getRightChnl(bean.getWebsite().getId(), ARTICLE_SYS, getCmsAdminId(), true);
        chnlList = SelectTreeUtils.handleTreeChild(chnlList);
        this.list = SelectTreeUtils.webTree(chnlList);
        addUploadRule();
        return EDIT;
    }
