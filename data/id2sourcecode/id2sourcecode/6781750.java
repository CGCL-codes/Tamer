    @Override
    protected Map<String, String> parseInit(Node init) throws RenderingException {
        Map<String, String> ret = new HashMap<String, String>();
        try {
            if (init != null) {
                Node request = ixml().evaluate(init, "./dt:request", ns()).item(0);
                ixml().validate(request, kernel().getPath("/schema/xml_data_backend.xsd"));
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
                    Node doc = ixml().evaluate(request, "./dt:doc", ns()).item(0);
                    if (doc != null) {
                        ret.put("uri", ixml().evaluate(doc, "./dt:uri/text()", ns()).item(0).getNodeValue());
                        NodeList nsl = ixml().evaluate(doc, "./dt:namespace", ns());
                        if (nsl.getLength() > 0) {
                            Node nsnode = nsl.item(0);
                            ret.put("ns_prefix", ixml().evaluate(nsnode, "./dt:prefix/text()", ns()).item(0).getNodeValue());
                            ret.put("ns_uri", ixml().evaluate(nsnode, "./dt:uri/text()", ns()).item(0).getNodeValue());
                            NodeList loc = ixml().evaluate(nsnode, "./dt:location/text()", ns());
                            if (loc.getLength() > 0) ret.put("ns_schema_location", loc.item(0).getNodeValue());
                        }
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
