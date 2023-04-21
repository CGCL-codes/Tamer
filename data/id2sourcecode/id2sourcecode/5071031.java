    private boolean vldArticleRight(Long id) {
        Article entity = articleMng.findById(id);
        if (entity == null) {
            addActionError("�����²����ڣ�" + id);
            return true;
        }
        CmsAdmin webAdmin = cmsAdminMng.getAdminByUserId(entity.getWebsite().getId(), getUserId());
        CmsAdmin inputAdmin = entity.getAdminInput();
        if (inputAdmin != null && getCmsAdmin().getSelfOnly() && !webAdmin.equals(inputAdmin)) {
            addActionError("����ά���������Լ�����ݣ�" + id);
            return true;
        }
        if (webAdmin == null || !entity.getChannel().getAdmins().contains(webAdmin)) {
            addActionError("��û���������������Ŀ��Ȩ�ޣ�" + id);
            return true;
        }
        return false;
    }
