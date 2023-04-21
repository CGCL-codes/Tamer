    public IChannel getChannel(Class<?> cclass) throws ModuleNotFoundException {
        if (_defaults.containsKey(cclass)) return _defaults.get(cclass); else {
            IXmlChannel ixml = (IXmlChannel) _defaults.get(IXmlChannel.class);
            try {
                Document doc = ixml.open(getPath("/config/default_modules.xml"), getPath("/schema/map.xsd"));
                NamespaceContext ns = new NamespaceContext();
                ns.setNamespace("map", "swemas/map");
                NodeList cands = ixml.evaluate(doc, "/map:map/map:pair[map:key=\"" + cclass.getName() + "\"]/map:value/text()", ns);
                if (cands.getLength() > 0) {
                    String cn = cands.item(0).getNodeValue();
                    IChannel ic = getModule(cn);
                    _defaults.putIfAbsent(cclass, ic);
                    return _defaults.get(cclass);
                } else throw new ModuleNotFoundException(null, this.getClass().getCanonicalName(), cclass.getName(), ErrorCode.DefaultsOpenError.getCode());
            } catch (XmlException e) {
                throw new ModuleNotFoundException(e, this.getClass().getCanonicalName(), cclass.getName(), ErrorCode.DefaultsOpenError.getCode());
            } catch (XPathExpressionException e) {
                throw new ModuleNotFoundException(e, this.getClass().getCanonicalName(), cclass.getName(), ErrorCode.NoDefaultModuleDefinedError.getCode());
            }
        }
    }
