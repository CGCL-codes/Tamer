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
                if (uri != null) {
                    String sp = parameters.get("ns_prefix");
                    String su = parameters.get("ns_uri");
                    String sl = parameters.get("ns_schema_location");
                    try {
                        IXmlChannel ixml = (IXmlChannel) kernel().getChannel(IXmlChannel.class);
                        if (sl != null && sp != null && su != null) {
                            ns().setNamespace(sp, su);
                            try {
                                NodeList ret = ixml.evaluate(ixml.open(uri, sl), qf, ns());
                                List<Node> rn = new ArrayList<Node>();
                                for (int i = 0; i < ret.getLength(); ++i) rn.add(ret.item(i));
                                ostream.put(anchor, rn);
                                oflow.put(stm, ostream);
                            } catch (XmlException e) {
                                try {
                                    IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                                    ed.event(new RenderingEvent(new RenderingException(e, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.ResourcesObtainingError.getCode())));
                                } catch (ModuleNotFoundException m) {
                                    IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                                    ed.event(new RenderingEvent(new RenderingException(e, name(new Locale("en", "US")), ErrorCode.ResourcesObtainingError.getCode())));
                                }
                                return oflow;
                            } catch (XPathExpressionException e) {
                                try {
                                    IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                                    ed.event(new RenderingEvent(new RenderingException(e, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.QueryError.getCode())));
                                } catch (ModuleNotFoundException m) {
                                    IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                                    ed.event(new RenderingEvent(new RenderingException(e, name(new Locale("en", "US")), ErrorCode.QueryError.getCode())));
                                }
                                return oflow;
                            }
                        } else if (sp != null && su != null) {
                            ns().setNamespace(sp, su);
                            try {
                                NodeList ret = ixml.evaluate(ixml.open(uri), qf, ns());
                                List<Node> rn = new ArrayList<Node>();
                                for (int i = 0; i < ret.getLength(); ++i) rn.add(ret.item(i));
                                ostream.put(anchor, rn);
                                oflow.put(stm, ostream);
                            } catch (XmlException e) {
                                try {
                                    IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                                    ed.event(new RenderingEvent(new RenderingException(e, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.ResourcesObtainingError.getCode())));
                                } catch (ModuleNotFoundException m) {
                                    IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                                    ed.event(new RenderingEvent(new RenderingException(e, name(new Locale("en", "US")), ErrorCode.ResourcesObtainingError.getCode())));
                                }
                                return oflow;
                            } catch (XPathExpressionException e) {
                                try {
                                    IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                                    ed.event(new RenderingEvent(new RenderingException(e, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.QueryError.getCode())));
                                } catch (ModuleNotFoundException m) {
                                    IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                                    ed.event(new RenderingEvent(new RenderingException(e, name(new Locale("en", "US")), ErrorCode.QueryError.getCode())));
                                }
                                return oflow;
                            }
                        } else {
                            try {
                                NodeList ret = ixml.evaluate(ixml.open(uri), qf);
                                List<Node> rn = new ArrayList<Node>();
                                for (int i = 0; i < ret.getLength(); ++i) rn.add(ret.item(i));
                                ostream.put(anchor, rn);
                                oflow.put(stm, ostream);
                            } catch (XmlException e) {
                                try {
                                    IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                                    ed.event(new RenderingEvent(new RenderingException(e, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.ResourcesObtainingError.getCode())));
                                } catch (ModuleNotFoundException m) {
                                    IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                                    ed.event(new RenderingEvent(new RenderingException(e, name(new Locale("en", "US")), ErrorCode.ResourcesObtainingError.getCode())));
                                }
                                return oflow;
                            } catch (XPathExpressionException e) {
                                try {
                                    IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                                    ed.event(new RenderingEvent(new RenderingException(e, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.QueryError.getCode())));
                                } catch (ModuleNotFoundException m) {
                                    IEventDispatchingChannel ed = (IEventDispatchingChannel) kernel().getChannel(IEventDispatchingChannel.class);
                                    ed.event(new RenderingEvent(new RenderingException(e, name(new Locale("en", "US")), ErrorCode.QueryError.getCode())));
                                }
                                return oflow;
                            }
                        }
                    } catch (ModuleNotFoundException e) {
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
                        return oflow;
                    }
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
