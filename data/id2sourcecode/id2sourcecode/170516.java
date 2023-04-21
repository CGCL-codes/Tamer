    public void list() throws IOException, TransformerConfigurationException {
        SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory.newInstance();
        Templates xslt = loadDcmDirXSL(tf);
        DirReader reader = fact.newDirReader(dirFile);
        reader.getFileSetInfo().writeFile(getTransformerHandler(tf, xslt), dict);
        try {
            list("", reader.getFirstRecord(onlyInUse), tf, xslt);
        } finally {
            reader.close();
        }
    }
