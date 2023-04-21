    public Object clone() {
        WSRPPortletImpl clone = new WSRPPortletImpl();
        clone.setPortletId(getPortletId());
        clone.setName(getName());
        clone.setChannelName(getChannelName());
        clone.setTitle(getTitle());
        clone.setShortTitle(getShortTitle());
        clone.setDisplayName(getDisplayName());
        clone.setKeywords(getKeywords());
        clone.setStatus(getStatus());
        clone.setProducerEntityId(getProducerEntityId());
        clone.setConsumerId(getConsumerId());
        clone.setPortletHandle(getPortletHandle());
        clone.setMimeTypes(getMimeTypes());
        return clone;
    }
