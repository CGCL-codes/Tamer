    public void list() throws IOException, TransformerConfigurationException {
        SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory.newInstance();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream in = cl.getResourceAsStream("DcmDir.xsl");
        Templates xslt = tf.newTemplates(new StreamSource(in));
        DirReader reader = fact.newDirReader(dirFile);
        reader.getFileSetInfo().writeFile(getTransformerHandler(tf, xslt), dict);
        try {
            list("", reader.getFirstRecord(onlyInUse), tf, xslt);
        } finally {
            reader.close();
        }
    }
