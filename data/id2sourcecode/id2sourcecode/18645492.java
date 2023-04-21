    private boolean vldDownloadRight(Long id) {
        Download entity = downloadMng.findById(id);
        if (entity == null) {
            addActionError("�����²����ڣ�" + id);
            return true;
        }
        if (getCmsAdmin().getSelfOnly() && !getCmsAdminId().equals(entity.getAdminInput().getId())) {
            addActionError("�����Լ�����ݲ����޸ģ�" + id);
            return true;
        }
        if (!entity.getChannel().adminsContain(getCmsAdminId())) {
            addActionError("��û���������������Ŀ��Ȩ�ޣ�" + id);
            return true;
        }
        return false;
    }
