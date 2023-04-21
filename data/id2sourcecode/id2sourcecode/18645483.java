    @SuppressWarnings("unchecked")
    public String edit() {
        this.bean = downloadMng.findById(id);
        contentCtgList = contentCtgMng.getList(getWebId(), false);
        tplContentList = bean.getChannel().getModel().tplContentList(getConfig(), DOWNLOAD_SYS, contextPvd.getAppRoot());
        downTypeList = downTypeMng.getList(getWebId(), false);
        List<CmsChannel> chnlList = cmsChannelMng.getRightChnl(bean.getWebsite().getId(), DOWNLOAD_SYS, getCmsAdminId(), true);
        chnlList = SelectTreeUtils.handleTreeChild(chnlList);
        this.list = SelectTreeUtils.webTree(chnlList);
        addUploadRule();
        return EDIT;
    }
