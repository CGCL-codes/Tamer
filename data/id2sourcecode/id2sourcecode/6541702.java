    public static WSRPPortlet toModel(WSRPPortletSoap soapModel) {
        WSRPPortlet model = new WSRPPortletImpl();
        model.setPortletId(soapModel.getPortletId());
        model.setName(soapModel.getName());
        model.setChannelName(soapModel.getChannelName());
        model.setTitle(soapModel.getTitle());
        model.setShortTitle(soapModel.getShortTitle());
        model.setDisplayName(soapModel.getDisplayName());
        model.setKeywords(soapModel.getKeywords());
        model.setStatus(soapModel.getStatus());
        model.setProducerEntityId(soapModel.getProducerEntityId());
        model.setConsumerId(soapModel.getConsumerId());
        model.setPortletHandle(soapModel.getPortletHandle());
        model.setMimeTypes(soapModel.getMimeTypes());
        return model;
    }
