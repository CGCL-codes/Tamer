    @Override
    public Map<String, Map<String, List<Node>>> render(Map<String, Map<String, List<Node>>> iflow, Map<String, String> params, Node init, HttpServletRequest request) throws RenderingException {
        Map<String, Map<String, List<Node>>> oflow = new HashMap<String, Map<String, List<Node>>>();
        Map<String, List<Node>> ostream = new HashMap<String, List<Node>>();
        String anchor = "";
        if (params.containsKey("anchor")) anchor = params.get("anchor");
        String stm = "";
        if (params.containsKey("stream")) stm = params.get("stream");
        try {
            Map<String, String> parameters = parseInit(init);
            String text = parameters.get("value");
            if (text != null) {
                if (parameters.containsKey("formatted") && parameters.get("formatted").equals("true") && request != null) text = String.format(text, request.getParameterMap().values().toArray());
                List<Node> ret = new ArrayList<Node>();
                Document doc;
                if (init != null) doc = init.getOwnerDocument(); else doc = ixml().create();
                if (parameters.containsKey("type") && parameters.get("type").equals("CDATA")) ret.add(doc.createCDATASection(text)); else ret.add(doc.createTextNode(text));
                ostream.put(anchor, ret);
                oflow.put(stm, ostream);
            } else {
                String qf = parameters.get("query");
                if (parameters.containsKey("formatted") && parameters.get("formatted").equals("true") && request != null) String.format(qf, request.getParameterMap().values());
                String uri = parameters.get("uri");
                if (uri == null) {
                }
            }
            return oflow;
        } catch (XmlException e) {
            try {
                IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                ed.event(new RenderingEvent(new RenderingException(e, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.ResourcesObtainingError.getCode())));
            } catch (ModuleNotFoundException m) {
                try {
                    IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                    ed.event(new RenderingEvent(new RenderingException(e, name(new Locale("en", "US")), ErrorCode.ResourcesObtainingError.getCode())));
                } catch (ModuleNotFoundException e1) {
                }
            }
            return oflow;
        } catch (IllegalFormatException ma) {
            try {
                IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                ed.event(new RenderingEvent(new RenderingException(ma, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.ResourcesObtainingError.getCode())));
            } catch (ModuleNotFoundException m) {
                try {
                    IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                    ed.event(new RenderingEvent(new RenderingException(ma, name(new Locale("en", "US")), ErrorCode.ResourcesObtainingError.getCode())));
                } catch (ModuleNotFoundException e) {
                }
            }
            return oflow;
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
            return oflow;
        }
    }
