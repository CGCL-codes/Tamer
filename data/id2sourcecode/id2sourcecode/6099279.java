    @Override
    protected Map<String, String> parseInit(Node init) throws RenderingException {
        Map<String, String> ret = new HashMap<String, String>();
        try {
            if (init != null) {
                Node request = ixml().evaluate(init, "./dt:request", ns()).item(0);
                ixml().validate(request, kernel().getPath("/schema/plain_data_backend.xsd"));
                boolean formatted = false;
                if (request.getAttributes().getNamedItem("formatted") != null) formatted = Boolean.parseBoolean(request.getAttributes().getNamedItem("formatted").getNodeValue());
                ret.put("formatted", Boolean.toString(formatted));
                String type = "text";
                if (request.getAttributes().getNamedItem("type") != null) type = request.getAttributes().getNamedItem("type").getNodeValue();
                ret.put("type", type);
                Node vl = ixml().evaluate(request, "./dt:value/text()", ns()).item(0);
                if (vl != null) ret.put("value", vl.getNodeValue());
                return ret;
            } else {
                try {
                    throw new RenderingException(null, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.InitializationError.getCode());
                } catch (ModuleNotFoundException m) {
                    throw new RenderingException(null, name(new Locale("en", "US")), ErrorCode.InitializationError.getCode());
                }
            }
        } catch (DOMException e) {
            try {
                throw new RenderingException(e, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.InternalError.getCode());
            } catch (ModuleNotFoundException m) {
                throw new RenderingException(e, name(new Locale("en", "US")), ErrorCode.InternalError.getCode());
            }
        } catch (XmlException e) {
            try {
                throw new RenderingException(e, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.InitializationError.getCode());
            } catch (ModuleNotFoundException m) {
                throw new RenderingException(e, name(new Locale("en", "US")), ErrorCode.InitializationError.getCode());
            }
        } catch (XPathExpressionException e) {
            try {
                throw new RenderingException(e, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.InitializationError.getCode());
            } catch (ModuleNotFoundException m) {
                throw new RenderingException(e, name(new Locale("en", "US")), ErrorCode.InitializationError.getCode());
            }
        }
    }
