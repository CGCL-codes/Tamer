    protected Map<String, Object> getMetaProps(final Page.Request pageRequest) throws WWWeeePortal.Exception {
        final Map<String, Object> metaProps = new HashMap<String, Object>();
        pageRequest.getMetaProps(metaProps);
        getPortal().getMetaProps(metaProps, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders());
        pageRequest.getPage().getMetaProps(pageRequest, metaProps);
        getChannel().getMetaProps(pageRequest, metaProps);
        return metaProps;
    }
