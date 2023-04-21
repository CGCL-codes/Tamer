    public ContentPanel openContentPanelContext(ContentPanelContext context) throws UnsupportedContextException {
        if (context.getSource() != null && context.getSource().isReleased()) {
            context.getSource().setReleased(false);
            try {
                context.getSource().setContext(context);
                return context.getSource();
            } catch (UnsupportedContextException uce) {
            }
        }
        if (contextClassContentPanelFactoryMap.containsKey(context.getClass())) {
            ContentPanelFactory cpf = (ContentPanelFactory) contextClassContentPanelFactoryMap.get(context.getClass());
            try {
                return cpf.openContentPanelContext(context);
            } catch (UnsupportedContextException uce) {
            }
        }
        String protocol = context.getURL().getProtocol();
        if (protocolNameContentPanelFactoryMap.containsKey(protocol)) {
            ContentPanelFactory cpf = (ContentPanelFactory) protocolNameContentPanelFactoryMap.get(protocol);
            try {
                return cpf.openContentPanelContext(context);
            } catch (UnsupportedContextException uce) {
            }
        }
        try {
            URLConnection urlc = context.getURL().openConnection();
            String type = urlc.getContentType();
            if (type != null) {
                if (mimeTypeContentPanelFactoryMap.containsKey(type)) {
                    ContentPanelFactory cpf = (ContentPanelFactory) mimeTypeContentPanelFactoryMap.get(type);
                    try {
                        return cpf.openContentPanelContext(context);
                    } catch (UnsupportedContextException uce) {
                    }
                }
                if (type.indexOf('/') != -1) {
                    String typePrefix = type.substring(0, type.indexOf('/'));
                    if (mimeTypeContentPanelFactoryMap.containsKey(typePrefix)) {
                        ContentPanelFactory cpf = (ContentPanelFactory) mimeTypeContentPanelFactoryMap.get(typePrefix);
                        try {
                            return cpf.openContentPanelContext(context);
                        } catch (UnsupportedContextException uce) {
                        }
                    }
                }
            }
            if (context.getURL().getPath().endsWith(".xml") || (type != null && type.endsWith(".xml"))) {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setNamespaceAware(true);
                DocumentBuilder db = dbf.newDocumentBuilder();
                Element docElement = db.parse(urlc.getInputStream()).getDocumentElement();
                String rootElementName;
                if (docElement.getNamespaceURI() == null) {
                    rootElementName = docElement.getTagName();
                } else {
                    rootElementName = "{" + docElement.getNamespaceURI() + "}" + docElement.getLocalName();
                }
                if (rootElementContentPanelFactoryMap.containsKey(rootElementName)) {
                    ContentPanelFactory cpf = (ContentPanelFactory) rootElementContentPanelFactoryMap.get(rootElementName);
                    try {
                        return cpf.openContentPanelContext(context);
                    } catch (UnsupportedContextException uce) {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new UnsupportedContextException(context.toString());
    }
