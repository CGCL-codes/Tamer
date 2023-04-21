    public CmsChannel updateChannel(CmsChannel chnl, CmsAdmin admin, Collection<CmsAdmin> admins, UploadRule rule) {
        CmsChannel entity = findById(chnl.getId());
        String origPath = entity.getPath();
        handleTitleImg(chnl);
        CmsChannel oparent = entity.getParent();
        CmsChannel cparent = chnl.getParent();
        if (oparent != null && cparent != null) {
            if (!oparent.getId().equals(cparent.getId())) {
                oparent.getChild().remove(entity);
                cparent.addToChild(entity);
                entity.setParent(cparent);
            }
        }
        chnl.setParent(null);
        if (admins != null && admins.size() > 0) {
            Set<CmsAdmin> oadmins = entity.getAdmins();
            for (CmsAdmin a : oadmins) {
                if (!admins.contains(a)) {
                    a.getChannels().remove(entity);
                }
            }
            for (CmsAdmin a : admins) {
                a.addTochannels(entity);
            }
            oadmins.clear();
            oadmins.addAll(admins);
        }
        updateByUpdater(createUpdate(chnl));
        entity.setHasChild(entity.getModel().getHasChild());
        removeAttachment(entity, false);
        if (!origPath.equals(entity.getPath())) {
            String sid = String.valueOf(entity.getWebsite().getId());
            idCacheSvc.remove(CmsChannel.REF, sid, origPath);
            idCacheSvc.put(entity.getId(), CmsChannel.REF, sid, entity.getPath());
        }
        return entity;
    }
