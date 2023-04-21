    public XmlUsingModule(IKernel kernel) throws InvocationTargetException {
        super(kernel);
        _ns = new NamespaceContext();
        try {
            _ixml = (IXmlChannel) kernel().getChannel(IXmlChannel.class);
        } catch (ModuleNotFoundException e) {
            throw new InvocationTargetException(e);
        }
    }
