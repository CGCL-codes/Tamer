    @Override
    public Map<String, Map<String, List<Node>>> render(Map<String, Map<String, List<Node>>> iflow, Map<String, String> params, Node init, HttpServletRequest request) {
        try {
            Map<String, List<Node>> ibodystream;
            if (iflow.containsKey("body")) ibodystream = iflow.get("body"); else ibodystream = new HashMap<String, List<Node>>();
            Map<String, List<Node>> iheadstream;
            if (iflow.containsKey("head")) iheadstream = iflow.get("head"); else iheadstream = new HashMap<String, List<Node>>();
            Map<String, List<Node>> obodystream = new HashMap<String, List<Node>>();
            Map<String, List<Node>> oheadstream = new HashMap<String, List<Node>>();
            Map<String, String> parameters = parseInit(init);
            List<Node> bnodes = new ArrayList<Node>();
            String tname = parameters.get("template");
            boolean injective = Boolean.valueOf(parameters.get("injective"));
            Document template = ixml().open(kernel().getPath("/template/" + tname), kernel().getPath("/schema/xhtml1-strict.xsd"));
            if (injective) {
                List<Map<String, Node>> parts = populateNodes(ibodystream);
                for (int i = 0; i < parts.size(); ++i) {
                    Map<String, Node> map = parts.get(i);
                    Node ret = template.cloneNode(true);
                    NodeList anchors = ixml().evaluate(ret, "/xhtml:html//xhtml:a[@class=\"swemas_content\"]", ns());
                    for (int j = 0; j < anchors.getLength(); ++j) {
                        Node a = anchors.item(j);
                        String name = a.getAttributes().getNamedItem("id").getNodeValue();
                        if (map.containsKey(name)) appendChild(a.getParentNode(), map.get(name));
                    }
                    NodeList newnodes = ixml().evaluate(ret, "xhtml:html/xhtml:body/*", ns());
                    for (int j = 0; j < newnodes.getLength(); ++j) bnodes.add(newnodes.item(j));
                }
            } else {
                NodeList anchors = ixml().evaluate(template, "/xhtml:html//xhtml:a[@class=\"swemas_content\"]", ns());
                for (int i = 0; i < anchors.getLength(); ++i) {
                    Node a = anchors.item(i);
                    String name = a.getAttributes().getNamedItem("id").getNodeValue();
                    if (ibodystream.containsKey(name)) {
                        List<Node> chnodes = ibodystream.get(name);
                        for (Node nd : chnodes) appendChild(a.getParentNode(), nd);
                    }
                }
                NodeList newnodes = ixml().evaluate(template, "xhtml:html/xhtml:body/*", ns());
                for (int i = 0; i < newnodes.getLength(); ++i) bnodes.add(newnodes.item(i));
            }
            String anchor = "";
            if (params.containsKey("anchor")) anchor = params.get("anchor");
            obodystream.put(anchor, bnodes);
            List<Node> hnodes = new ArrayList<Node>();
            for (List<Node> list : iheadstream.values()) for (Node n : list) hnodes.add(n);
            NodeList headnodes = ixml().evaluate(template, "xhtml:html/xhtml:head/*", ns());
            for (int i = 0; i < headnodes.getLength(); ++i) hnodes.add(headnodes.item(i));
            oheadstream.put("", hnodes);
            Map<String, Map<String, List<Node>>> oflow = new HashMap<String, Map<String, List<Node>>>();
            oflow.put("body", obodystream);
            oflow.put("head", oheadstream);
            return oflow;
        } catch (XPathExpressionException e) {
            try {
                IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                ed.event(new RenderingEvent(new RenderingException(e, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.TemplateProcessingError.getCode())));
            } catch (ModuleNotFoundException m) {
                try {
                    IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                    ed.event(new RenderingEvent(new RenderingException(e, name(new Locale("en", "US")), ErrorCode.TemplateProcessingError.getCode())));
                } catch (ModuleNotFoundException m1) {
                }
            }
            iflow.remove("body");
            return new HashMap<String, Map<String, List<Node>>>(iflow);
        } catch (XmlException e) {
            try {
                IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                ed.event(new RenderingEvent(new RenderingException(e, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.TemplateProcessingError.getCode())));
            } catch (ModuleNotFoundException m) {
                try {
                    IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                    ed.event(new RenderingEvent(new RenderingException(e, name(new Locale("en", "US")), ErrorCode.TemplateProcessingError.getCode())));
                } catch (ModuleNotFoundException m1) {
                }
            }
            iflow.remove("body");
            return new HashMap<String, Map<String, List<Node>>>(iflow);
        } catch (RenderingException e) {
            try {
                IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                ed.event(new RenderingEvent(e));
            } catch (ModuleNotFoundException m) {
                try {
                    IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                    ed.event(new RenderingEvent(e));
                } catch (ModuleNotFoundException m1) {
                }
            }
            iflow.remove("body");
            return new HashMap<String, Map<String, List<Node>>>(iflow);
        }
    }
