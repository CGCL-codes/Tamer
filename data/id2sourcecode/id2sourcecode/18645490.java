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
        if (vldDownloadRight(bean.getId())) {
            return true;
        }
        if (vldChannel(bean.getChannel().getId(), false, null)) {
            return true;
        }
        if (vldWebsite(bean.getId(), bean)) {
            return true;
        }
        if (vldContentCtg(bean.getContentCtg().getId(), null)) {
            return true;
        }
        return false;
    }
