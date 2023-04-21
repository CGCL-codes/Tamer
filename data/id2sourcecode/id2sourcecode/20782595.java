    @Override
    protected String content(String chnlName, Long id) {
        CmsChannel cmschnl = cmsChannelMng.getByPath(getWebId(), chnlName);
        if (cmschnl != null) {
            if (cmschnl.getSysType().equals(Constants.ARTICLE_SYS)) {
                arti = articleMng.findAndCheckResPath(id);
                String err = checkArticle(arti, chnlName);
                if (err != null) {
                    return err;
                }
                chnl = arti.getChannel();
                sysType = chnl.getSysType();
                pagination = new SimplePage(pageNo, 1, arti.getPageCount());
                arti.updateVisit(1);
                tplPath = arti.chooseTpl();
            } else {
                downcontent(chnlName, id);
            }
        } else {
            return pageNotFound();
        }
        return SUCCESS;
    }
