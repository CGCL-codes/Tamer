    @Override
    protected Map<String, String> parseInit(Node init) throws RenderingException {
        Map<String, String> ret = new HashMap<String, String>();
        try {
            if (init != null) {
                Node request = ixml().evaluate(init, "./dt:request", ns()).item(0);
                ixml().validate(request, kernel().getPath("/schema/db_data_backend.xsd"));
                boolean formatted = false;
                if (request.getAttributes().getNamedItem("formatted") != null) formatted = Boolean.parseBoolean(request.getAttributes().getNamedItem("formatted").getNodeValue());
                ret.put("formatted", Boolean.toString(formatted));
                String type = "text";
                if (request.getAttributes().getNamedItem("type") != null) type = request.getAttributes().getNamedItem("type").getNodeValue();
                ret.put("type", type);
                NodeList vl = ixml().evaluate(request, "./dt:value/text()", ns());
                if (vl.getLength() > 0) ret.put("value", vl.item(0).getNodeValue()); else {
                    NodeList ql = ixml().evaluate(request, "./dt:query/text()", ns());
                    ret.put("query", ql.item(0).getNodeValue());
                    Node server = ixml().evaluate(request, "./dt:server", ns()).item(0);
                    if (server != null) {
                        ret.put("server", ixml().evaluate(server, "./dt:host/text()", ns()).item(0).getNodeValue());
                        ret.put("port", ixml().evaluate(server, "./dt:port/text()", ns()).item(0).getNodeValue());
                        ret.put("login", ixml().evaluate(server, "./dt:login/text()", ns()).item(0).getNodeValue());
                        ret.put("pass", ixml().evaluate(server, "./dt:pass/text()", ns()).item(0).getNodeValue());
                    }
                }
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
