    public boolean validateUpdate() {
        if (hasErrors()) {
            return true;
        }
        if (vldUploadRule()) {
            return true;
        }
        if (vldBean()) {
            return true;
        }
        if (vldArticleRight(bean.getId())) {
            return true;
        }
        Article entity = articleMng.findById(bean.getId());
        Long webId = entity.getWebsite().getId();
        if (vldChannel(bean.getChannel().getId(), false, null, webId)) {
            return true;
        }
        if (vldContentCtg(bean.getContentCtg().getId(), null)) {
            return true;
        }
        return false;
    }
