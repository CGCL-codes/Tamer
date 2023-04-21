    @SuppressWarnings("unchecked")
    public void setUpPost(Element request, IBizDriver driver, List<HttpConfigInfo> list) throws XAwareException {
        HttpConfigInfo config = new HttpConfigInfo();
        IChannelSpecification spec = driver.getChannelSpecification();
        config.setUrl(spec.getProperty(XAwareConstants.BIZDRIVER_URL));
        config.setRequestType(RequestType.POST);
        setUpCommonGetPost(request, spec, config);
        config.setBoundary(request.getAttributeValue(XAwareConstants.BIZCOMPONENT_ATTR_BOUNDARY, ns));
        list.add(config);
    }
