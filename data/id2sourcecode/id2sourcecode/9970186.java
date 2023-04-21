    private static List<FactoryList> parseLegacyXmlFile(List<FactoryList> list) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> enumeration = classloader.getResources(IF_PLUGIN_PATH);
        SAXParserFactory saxparserfactory = SAXParserFactory.newInstance();
        saxparserfactory.setNamespaceAware(true);
        XMLReader xmlreader = saxparserfactory.newSAXParser().getXMLReader();
        ImplFactoryParsingHandler implfactoryparsinghandler = new ImplFactoryParsingHandler();
        xmlreader.setContentHandler(implfactoryparsinghandler);
        xmlreader.setDTDHandler(implfactoryparsinghandler);
        xmlreader.setEntityResolver(implfactoryparsinghandler);
        xmlreader.setErrorHandler(implfactoryparsinghandler);
        System.out.println("Enum:");
        do {
            if (!enumeration.hasMoreElements()) break;
            URL url = (URL) enumeration.nextElement();
            System.out.println(" - " + url);
            try {
                xmlreader.parse(new InputSource(url.openStream()));
                FactoryList factorylist = new FactoryList();
                factorylist.rank = implfactoryparsinghandler.rank;
                factorylist.factories.addAll(implfactoryparsinghandler.getFactories());
                list.add(factorylist);
            } catch (Exception exception) {
            }
        } while (true);
        return list;
    }
