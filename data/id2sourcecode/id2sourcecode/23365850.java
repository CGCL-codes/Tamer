    public CmsAcquisitionTemp save(CmsAcquisitionTemp bean) {
        clear(bean.getSite().getId(), bean.getChannelUrl());
        dao.save(bean);
        return bean;
    }
