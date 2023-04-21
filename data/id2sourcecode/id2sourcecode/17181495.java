    public boolean hasPermission(Hashtable extrAuths) throws Exception {
        if (operates == null) {
            return false;
        }
        boolean result = true;
        try {
            Pepodom pepodom = new Pepodom();
            for (int i = 0; i < operates.length; i++) {
                pepodom.setLoginProvider(userId);
                int resId = 0;
                if (treeNode.getType().equalsIgnoreCase("site")) {
                    resId = Const.SITE_TYPE_RES + ((Site) treeNode).getSiteID();
                } else if (treeNode.getType().equalsIgnoreCase("type")) {
                    resId = Const.DOC_TYPE_RES + ((DocType) treeNode).getDocTypeID();
                } else {
                    resId = Const.CHANNEL_TYPE_RES + ((Channel) treeNode).getChannelID();
                }
                pepodom.setResID(Integer.toString(resId));
                pepodom.setOperateID(operates[i]);
                if (!pepodom.isDisplay(extrAuths)) {
                    result = false;
                    break;
                }
            }
        } catch (Exception ex) {
            log.error("��ѯ����Ȩ��ʧ��!", ex);
            throw ex;
        }
        return result;
    }
