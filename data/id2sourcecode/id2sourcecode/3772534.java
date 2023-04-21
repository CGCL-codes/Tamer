    protected Map<String, Map<String, List<Node>>> parseElement(Node node, HttpServletRequest request) throws RenderingException {
        try {
            String cname = parseChannelName(node);
            Map<String, Map<String, List<Node>>> iflow = new HashMap<String, Map<String, List<Node>>>();
            NodeList childs = ixml().evaluate(node, "./tp:content/tp:element", ns());
            for (int i = 0; i < childs.getLength(); ++i) {
                Map<String, Map<String, List<Node>>> oflow = parseElement(childs.item(i), request);
                for (String flowname : oflow.keySet()) if (iflow.containsKey(flowname)) {
                    Map<String, List<Node>> ostream = oflow.get(flowname);
                    Map<String, List<Node>> istream = iflow.get(flowname);
                    for (String anchname : ostream.keySet()) if (istream.containsKey(anchname)) istream.get(anchname).addAll(ostream.get(anchname)); else istream.put(anchname, ostream.get(anchname));
                } else iflow.put(flowname, oflow.get(flowname));
            }
            Node init = ixml().evaluate(node, "./tp:init", ns()).item(0);
            Map<String, String> params = new HashMap<String, String>();
            if (node.getAttributes().getNamedItem("qname") != null) params.put("qname", node.getAttributes().getNamedItem("qname").getNodeValue());
            if (node.getAttributes().getNamedItem("anchor") != null) params.put("anchor", node.getAttributes().getNamedItem("anchor").getNodeValue());
            if (node.getAttributes().getNamedItem("namespace") != null) params.put("namespace", node.getAttributes().getNamedItem("namespace").getNodeValue());
            if (node.getAttributes().getNamedItem("stream") != null) params.put("stream", node.getAttributes().getNamedItem("stream").getNodeValue());
            IFlowRenderingChannel module = (IFlowRenderingChannel) kernel().getChannel(cname);
            return module.render(iflow, params, init, request);
        } catch (XPathExpressionException e) {
            try {
                throw new RenderingException(e, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.QueryError.getCode());
            } catch (ModuleNotFoundException m) {
                throw new RenderingException(e, name(new Locale("en", "US")), ErrorCode.QueryError.getCode());
            }
        } catch (ModuleNotFoundException e) {
            try {
                throw new RenderingException(e, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.CallError.getCode());
            } catch (ModuleNotFoundException m) {
                throw new RenderingException(e, name(new Locale("en", "US")), ErrorCode.CallError.getCode());
            }
        }
    }
