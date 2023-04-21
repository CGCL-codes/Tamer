    @Override
    public CmsChannel deleteById(Serializable id) {
        CmsChannel entity = findById(id);
        for (CmsAdmin admin : entity.getAdmins()) {
            admin.getChannels().remove(entity);
        }
        CmsChannel parent = entity.getParent();
        super.delete(entity);
        if (parent != null) {
            parent.getChild().remove(entity);
        }
        removeAttachment(entity, true);
        String sid = String.valueOf(entity.getWebsite().getId());
        idCacheSvc.remove(CmsChannel.REF, sid, entity.getPath());
        if (parent == null) {
            idCacheSvc.remove(CmsChannel.REF, sid, "", entity.getSysType());
        }
        return entity;
    }
