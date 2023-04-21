    @SuppressWarnings("unchecked")
    public void setUpMulti(Element request, IBizDriver driver, List<HttpConfigInfo> list) throws XAwareException {
        IChannelSpecification spec = driver.getChannelSpecification();
        List<Element> elements = request.getChildren();
        for (Element child : elements) {
            if (child.getNamespace().equals(XAwareConstants.xaNamespace)) {
                if (XAwareConstants.BIZCOMPONENT_HTTP_GET.equals(child.getName())) {
                    list.add(setUpOneMultiGet(child, spec));
                } else if (XAwareConstants.BIZCOMPONENT_HTTP_POST.equals(child.getName())) {
                    list.add(setUpOneMultiPost(child, spec));
                }
            }
        }
    }
