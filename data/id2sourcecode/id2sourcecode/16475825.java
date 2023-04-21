    public static WSRPPortletSoap toSoapModel(WSRPPortlet model) {
        WSRPPortletSoap soapModel = new WSRPPortletSoap();
        soapModel.setPortletId(model.getPortletId());
        soapModel.setName(model.getName());
        soapModel.setChannelName(model.getChannelName());
        soapModel.setTitle(model.getTitle());
        soapModel.setShortTitle(model.getShortTitle());
        soapModel.setDisplayName(model.getDisplayName());
        soapModel.setKeywords(model.getKeywords());
        soapModel.setStatus(model.getStatus());
        soapModel.setProducerEntityId(model.getProducerEntityId());
        soapModel.setConsumerId(model.getConsumerId());
        soapModel.setPortletHandle(model.getPortletHandle());
        soapModel.setMimeTypes(model.getMimeTypes());
        return soapModel;
    }
