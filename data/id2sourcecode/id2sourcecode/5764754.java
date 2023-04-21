    @Override
    public Map<String, Map<String, List<Node>>> render(Map<String, Map<String, List<Node>>> iflow, Map<String, String> params, Node init, HttpServletRequest request) throws RenderingException {
        String stm = "";
        try {
            Map<String, List<Node>> istream;
            if (iflow.containsKey(stm)) istream = iflow.get(stm); else istream = new HashMap<String, List<Node>>();
            Map<String, List<Node>> ostream = new HashMap<String, List<Node>>();
            List<Node> nodes = new ArrayList<Node>();
            Document doc;
            if (init != null) doc = init.getOwnerDocument(); else doc = ixml().create();
            List<Node> chnodes = istream.get("");
            List<String> values = new ArrayList<String>();
            for (Node n : chnodes) values.add(n.getNodeValue());
            Gson gson = new Gson();
            nodes.add(doc.createTextNode(gson.toJson(values.toArray())));
            ostream.put("", nodes);
            Map<String, Map<String, List<Node>>> oflow = new HashMap<String, Map<String, List<Node>>>();
            oflow.put(stm, ostream);
            for (String stmname : iflow.keySet()) if (!stmname.equals(stm)) oflow.put(stmname, iflow.get(stmname));
            return oflow;
        } catch (XmlException e) {
            try {
                IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                ed.event(new RenderingEvent(new RenderingException(e, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.InternalError.getCode())));
            } catch (ModuleNotFoundException m) {
                try {
                    IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                    ed.event(new RenderingEvent(new RenderingException(e, name(new Locale("en", "US")), ErrorCode.InternalError.getCode())));
                } catch (ModuleNotFoundException m1) {
                }
            }
            iflow.remove(stm);
            return new HashMap<String, Map<String, List<Node>>>(iflow);
        } catch (DOMException e) {
            try {
                IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                ed.event(new RenderingEvent(new RenderingException(e, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.ResultComposingError.getCode())));
            } catch (ModuleNotFoundException m) {
                try {
                    IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                    ed.event(new RenderingEvent(new RenderingException(e, name(new Locale("en", "US")), ErrorCode.ResultComposingError.getCode())));
                } catch (ModuleNotFoundException m1) {
                }
            }
            iflow.remove(stm);
            return new HashMap<String, Map<String, List<Node>>>(iflow);
        }
    }
