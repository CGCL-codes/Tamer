    private String checkArticle(Article entity, String chnlName) {
        if (entity == null || !entity.getChannel().getPath().equals(chnlName)) {
            return pageNotFound();
        }
        CmsMemberGroup group = entity.getGroup();
        if (group == null) {
            group = entity.getChannel().getGroupVisit();
        }
        if (group != null) {
            CmsMember cmsMember = getCmsMember();
            if (cmsMember == null) {
                return redirectLogin();
            }
            int artiLevel = group.getLevel();
            CmsMemberGroup memberGroup = cmsMember.getGroup();
            int memberLevel = memberGroup.getLevel();
            if (artiLevel > memberLevel) {
                addActionError("��Ļ�Ա�鼶���ǡ�" + memberGroup.getName() + "������ҳ����Ҫ��" + group.getName() + "�������ϼ�����ܷ���");
                return showMessage();
            }
        }
        if (entity.getDisabled()) {
            addActionError("������ʵ������Ѿ����ر�");
            return showMessage();
        }
        return null;
    }
