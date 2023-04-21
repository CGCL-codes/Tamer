    public void startResponseReqBody() {
        MappingResult mapping = getRequestMapping();
        String path = mapping.getResolvePath();
        if (PAC_PAGE.equals(path)) {
            responseProxyPac();
            return;
        }
        mapping.setDesitinationFile(config.getPublicDocumentRoot());
        WebServerHandler response = (WebServerHandler) forwardHandler(Mapping.FILE_SYSTEM_HANDLER);
        logger.debug("responseObject:cid:" + getChannelId() + ":" + response + ":" + this);
    }
