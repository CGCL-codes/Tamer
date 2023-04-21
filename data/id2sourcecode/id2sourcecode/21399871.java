    @Override
    public Map<String, Map<String, List<Node>>> render(Map<String, Map<String, List<Node>>> iflow, Map<String, String> params, Node init, HttpServletRequest request) throws RenderingException {
        Map<String, Map<String, List<Node>>> oflow = new HashMap<String, Map<String, List<Node>>>();
        Map<String, List<Node>> ostream = new HashMap<String, List<Node>>();
        String qname = "";
        if (params.containsKey("qname")) qname = params.get("qname");
        String stm = "";
        if (params.containsKey("stream")) stm = params.get("stream");
        String namespace = "";
        if (params.containsKey("namespace")) namespace = params.get("namespace");
        try {
            Document doc = ixml().create();
            List<Node> out = new ArrayList<Node>();
            Node attr;
            if (namespace != "") attr = doc.createAttributeNS(namespace, qname); else attr = doc.createAttribute(qname);
            if (iflow.containsKey("")) {
                Map<String, List<Node>> tmap = iflow.get("");
                if (tmap.containsKey("")) {
                    Node data = tmap.get("").get(0);
                    if (data != null) if (data.getNodeType() == Node.TEXT_NODE) attr.setNodeValue(data.getNodeValue());
                }
            }
            out.add(attr);
            ostream.put("", out);
            oflow.put(stm, ostream);
            return oflow;
        } catch (XmlException e) {
            try {
                IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                ed.event(new RenderingEvent(new RenderingException(null, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.InternalError.getCode())));
            } catch (ModuleNotFoundException m) {
                try {
                    IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                    ed.event(new RenderingEvent(new RenderingException(null, name(new Locale("en", "US")), ErrorCode.InternalError.getCode())));
                } catch (ModuleNotFoundException m1) {
                }
            }
            return oflow;
        }
    }
