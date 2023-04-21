    public Channel update(Channel bean, ChannelExt ext, ChannelTxt txt, Integer[] viewGroupIds, Integer[] contriGroupIds, Integer[] userIds, Integer parentId, Map<String, String> attr) {
        Updater<Channel> updater = new Updater<Channel>(bean);
        bean = dao.updateByUpdater(updater);
        Channel parent;
        if (parentId != null) {
            parent = findById(parentId);
        } else {
            parent = null;
        }
        bean.setParent(parent);
        channelExtMng.update(ext);
        channelTxtMng.update(txt, bean);
        Map<String, String> attrOrig = bean.getAttr();
        attrOrig.clear();
        attrOrig.putAll(attr);
        for (CmsGroup g : bean.getViewGroups()) {
            g.getViewChannels().remove(bean);
        }
        bean.getViewGroups().clear();
        if (viewGroupIds != null && viewGroupIds.length > 0) {
            CmsGroup g;
            for (Integer gid : viewGroupIds) {
                g = cmsGroupMng.findById(gid);
                bean.addToViewGroups(g);
            }
        }
        for (CmsGroup g : bean.getContriGroups()) {
            g.getContriChannels().remove(bean);
        }
        bean.getContriGroups().clear();
        if (contriGroupIds != null && contriGroupIds.length > 0) {
            CmsGroup g;
            for (Integer gid : contriGroupIds) {
                g = cmsGroupMng.findById(gid);
                bean.addToContriGroups(g);
            }
        }
        for (CmsUser u : bean.getUsers()) {
            u.getChannels().remove(bean);
        }
        bean.getUsers().clear();
        if (userIds != null && userIds.length > 0) {
            CmsUser u;
            for (Integer uid : userIds) {
                u = cmsUserMng.findById(uid);
                bean.addToUsers(u);
            }
        }
        return bean;
    }
