    public void setUpDelete(Element request, IBizDriver driver, List<HttpConfigInfo> list) throws XAwareException {
        HttpConfigInfo config = new HttpConfigInfo();
        IChannelSpecification spec = driver.getChannelSpecification();
        config.setUrl(spec.getProperty(XAwareConstants.BIZDRIVER_URL));
        config.setRequestType(RequestType.DELETE);
        setUpCommonGetPost(request, spec, config);
        list.add(config);
    }
