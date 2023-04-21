    @BeforeClass
    public static void readExportDoc() throws ParserConfigurationException, SAXException, IOException {
        URL url = TestPTSWImport.class.getResource("/testData/ptswExport.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        ptswExport = db.parse(url.openStream());
    }
