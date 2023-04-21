    public MModel loadModelFromXMI(URL url) throws IOException {
        ZipInputStream zis = new ZipInputStream(url.openStream());
        String name = zis.getNextEntry().getName();
        while (!name.endsWith(".xmi")) {
            name = zis.getNextEntry().getName();
        }
        Argo.log.info("Loading Model from " + url);
        XMIReader xmiReader = null;
        try {
            xmiReader = new org.argouml.xml.xmi.XMIReader();
        } catch (SAXException se) {
        } catch (ParserConfigurationException pc) {
        }
        MModel mmodel = null;
        InputSource source = new InputSource(zis);
        source.setEncoding("UTF-8");
        try {
            mmodel = xmiReader.parse(new InputSource(zis));
        } catch (ClassCastException cc) {
            ArgoParser.SINGLETON.setLastLoadStatus(false);
            ArgoParser.SINGLETON.setLastLoadMessage("XMI file " + url.toString() + " could not be " + "parsed.");
        }
        if (xmiReader.getErrors()) {
            ArgoParser.SINGLETON.setLastLoadStatus(false);
            ArgoParser.SINGLETON.setLastLoadMessage("XMI file " + url.toString() + " could not be " + "parsed.");
        }
        UmlHelper.getHelper().addListenersToModel(mmodel);
        try {
            addMember(mmodel);
        } catch (PropertyVetoException pv) {
            throw new IOException("The model from XMI file" + url.toString() + "could not be added to the project.");
        }
        _UUIDRefs = new HashMap(xmiReader.getXMIUUIDToObjectMap());
        return mmodel;
    }
