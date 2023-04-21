    protected Map<String, String> parseInit(Node init) throws RenderingException {
        Map<String, String> params = new HashMap<String, String>();
        try {
            if (init != null) {
                NodeList pms = ixml().evaluate(init, "./parameter", ns());
                for (int i = 0; i < pms.getLength(); ++i) {
                    String name = ixml().evaluate(pms.item(i), "./@name", ns()).item(0).getNodeValue();
                    String value = ixml().evaluate(pms.item(i), "./text()", ns()).item(0).getNodeValue();
                    params.put(name, value);
                }
                return params;
            } else {
                try {
                    throw new RenderingException(null, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.InitializationError.getCode());
                } catch (ModuleNotFoundException m) {
                    throw new RenderingException(null, name(new Locale("en", "US")), ErrorCode.InitializationError.getCode());
                }
            }
        } catch (XPathExpressionException e) {
            try {
                throw new RenderingException(null, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.InitializationError.getCode());
            } catch (ModuleNotFoundException m) {
                throw new RenderingException(null, name(new Locale("en", "US")), ErrorCode.InitializationError.getCode());
            }
        }
    }
