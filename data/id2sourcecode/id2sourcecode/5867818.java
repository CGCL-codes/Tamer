    @Override
    public List<Node> render(Node node, HttpServletRequest request) throws RenderingException {
        try {
            String sc = "";
            sc = "xr_template_div_injective.xml";
            Document tpg = ixml().open(kernel().getPath("/site/pages/" + sc), kernel().getPath("/schema/topology.xsd"));
            Node root = ixml().evaluate(tpg, "/tp:element", ns()).item(0);
            if (root != null) {
                Map<String, Map<String, List<Node>>> flow = parseElement(root, request);
                Document json = ixml().create();
                DOMConfiguration config = json.getDomConfig();
                config.setParameter("namespaces", Boolean.TRUE);
                config.setParameter("namespace-declarations", Boolean.TRUE);
                Map<String, List<Node>> commonstream;
                if (flow.containsKey("")) commonstream = flow.get(""); else commonstream = new HashMap<String, List<Node>>();
                String text = "";
                for (List<Node> list : commonstream.values()) for (Node n : list) text += n.getNodeValue();
                appendChild(json, json.createTextNode(text));
                List<Node> out = new ArrayList<Node>();
                out.add(json);
                return out;
            } else {
                try {
                    throw new RenderingException(null, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.ResourcesObtainingError.getCode());
                } catch (ModuleNotFoundException m) {
                    throw new RenderingException(null, name(new Locale("en", "US")), ErrorCode.ResourcesObtainingError.getCode());
                }
            }
        } catch (XmlException e) {
            try {
                throw new RenderingException(e, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.InternalError.getCode());
            } catch (ModuleNotFoundException m) {
                throw new RenderingException(e, name(new Locale("en", "US")), ErrorCode.InternalError.getCode());
            }
        } catch (XPathExpressionException e) {
            try {
                throw new RenderingException(e, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.InternalError.getCode());
            } catch (ModuleNotFoundException m) {
                throw new RenderingException(e, name(new Locale("en", "US")), ErrorCode.InternalError.getCode());
            }
        } catch (DOMException e) {
            try {
                throw new RenderingException(e, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.InternalError.getCode());
            } catch (ModuleNotFoundException m) {
                throw new RenderingException(e, name(new Locale("en", "US")), ErrorCode.InternalError.getCode());
            }
        }
    }
