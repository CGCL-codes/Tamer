    public WSRPPortlet toEscapedModel() {
        if (isEscapedModel()) {
            return (WSRPPortlet) this;
        } else {
            WSRPPortlet model = new WSRPPortletImpl();
            model.setNew(isNew());
            model.setEscapedModel(true);
            model.setPortletId(getPortletId());
            model.setName(HtmlUtil.escape(getName()));
            model.setChannelName(HtmlUtil.escape(getChannelName()));
            model.setTitle(HtmlUtil.escape(getTitle()));
            model.setShortTitle(HtmlUtil.escape(getShortTitle()));
            model.setDisplayName(HtmlUtil.escape(getDisplayName()));
            model.setKeywords(HtmlUtil.escape(getKeywords()));
            model.setStatus(getStatus());
            model.setProducerEntityId(HtmlUtil.escape(getProducerEntityId()));
            model.setConsumerId(HtmlUtil.escape(getConsumerId()));
            model.setPortletHandle(HtmlUtil.escape(getPortletHandle()));
            model.setMimeTypes(HtmlUtil.escape(getMimeTypes()));
            model = (WSRPPortlet) Proxy.newProxyInstance(WSRPPortlet.class.getClassLoader(), new Class[] { WSRPPortlet.class }, new ReadOnlyBeanHandler(model));
            return model;
        }
    }
