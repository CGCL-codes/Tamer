    public boolean validateSave() {
        if (hasErrors()) {
            return true;
        }
        if (vldUploadRule()) {
            return true;
        }
        if (vldBean()) {
            return true;
        }
        bean.setWebsite(getWeb());
        bean.setConfig(getConfig());
        if (vldChannel(bean.getChannel().getId(), false, bean, getWebId())) {
            return true;
        }
        if (vldContentCtg(bean.getContentCtg().getId(), bean)) {
            return true;
        }
        if (vldMemberGroup(bean.getGroup(), bean, true)) {
            return true;
        }
        return false;
    }
