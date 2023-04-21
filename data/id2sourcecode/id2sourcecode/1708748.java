    public static void main(final String[] args) throws Exception {
        final File gameXmlFile = new FileOpen("Select xml file", ".xml").getFile();
        if (gameXmlFile == null) {
            System.out.println("No file selected");
            return;
        }
        final InputStream source = XmlUpdater.class.getResourceAsStream("gameupdate.xslt");
        if (source == null) {
            throw new IllegalStateException("Could not find xslt file");
        }
        final Transformer trans = TransformerFactory.newInstance().newTransformer(new StreamSource(source));
        final InputStream gameXmlStream = new BufferedInputStream(new FileInputStream(gameXmlFile));
        ByteArrayOutputStream resultBuf;
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            final URL url = XmlUpdater.class.getResource("");
            final String system = url.toExternalForm();
            final Source xmlSource = new StreamSource(gameXmlStream, system);
            resultBuf = new ByteArrayOutputStream();
            trans.transform(xmlSource, new StreamResult(resultBuf));
        } finally {
            gameXmlStream.close();
        }
        gameXmlFile.renameTo(new File(gameXmlFile.getAbsolutePath() + ".backup"));
        new FileOutputStream(gameXmlFile).write(resultBuf.toByteArray());
        System.out.println("Successfully updated:" + gameXmlFile);
    }
