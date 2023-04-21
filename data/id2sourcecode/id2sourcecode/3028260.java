    public void xtestMarshal() throws JiBXException, SAXException, IOException {
        GetRepositoryItemResponse o = new GetRepositoryItemResponse();
        String response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<csw:GetRecordByIdResponse xmlns:csw=\"http://www.opengis.net/cat/csw/2.0.2\">\n" + "  <csw:Record\n" + "  xmlns:ows=\"http://www.opengis.net/ows\" xmlns:gmd=\"http://www.isotc211.org/2005/gmd\"\n" + "  xmlns:gco=\"http://www.isotc211.org/2005/gco\"\n" + "  xmlns:dct=\"http://purl.org/dc/terms/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" + "    <dc:identifier>4cbce5e2-e8bc-4c4e-83d0-e1af7384758d</dc:identifier>\n" + "    <dc:title>\n" + "    Hydrological Basins in Africa (Sample record, please remove!)\n" + "    </dc:title>\n" + "    <dc:subject>watersheds</dc:subject>\n" + "    <dc:subject>river basins</dc:subject>\n" + "    <dc:subject>water resources</dc:subject>\n" + "    <dc:subject>hydrology</dc:subject>\n" + "    <dc:subject>AQUASTAT</dc:subject>\n" + "    <dc:subject>AWRD</dc:subject>\n" + "    <dc:subject>Africa</dc:subject>\n" + "    <dct:abstract>Major hydrological basins and their sub-basins. This dataset d\n" + "ivides the African continent according to its hydrological characteristics.\n" + "The dataset consists of the following information:- numerical code and name of t\n" + "he major basin (MAJ_BAS and MAJ_NAME); - area of the major basin in square km (M\n" + "AJ_AREA); - numerical code and name of the sub-basin (SUB_BAS and SUB_NAME); - a\n" + "rea of the sub-basin in square km (SUB_AREA); - numerical code of the sub-basin\n" + "towards which the sub-basin flows (TO_SUBBAS) (the codes -888 and -999 have been\n" + " assigned respectively to internal sub-basins and to sub-basins draining into th\n" + "e sea)</dct:abstract>\n" + "    <dc:language>eng</dc:language>\n" + "    <dc:format>ShapeFile</dc:format>\n" + "    <ows:BoundingBox crs=\"::WGS 1984\">\n" + "      <ows:LowerCorner>51.1 -34.6</ows:LowerCorner>\n" + "      <ows:UpperCorner>-17.3 38.2</ows:UpperCorner>\n" + "    </ows:BoundingBox>\n" + "  </csw:Record>\n" + "</csw:GetRecordByIdResponse>";
        o.setResponse(response);
        IBindingFactory bfact = BindingDirectory.getFactory(GetRepositoryItemResponse.class);
        IMarshallingContext marshallingContext = bfact.createMarshallingContext();
        Writer outConsole = new BufferedWriter(new OutputStreamWriter(System.out));
        marshallingContext.setOutput(outConsole);
        marshallingContext.setIndent(3);
        marshallingContext.marshalDocument(o, "UTF-8", null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer out = new BufferedWriter(new OutputStreamWriter(outputStream));
        marshallingContext.setIndent(3);
        marshallingContext.setOutput(out);
        marshallingContext.marshalDocument(o, "UTF-8", null);
        String result = outputStream.toString();
        InputSource marshallingResult = new InputSource(new ByteArrayInputStream(outputStream.toByteArray()));
        FileInputStream fis = new FileInputStream(new File("src/test/resources/csw202-discovery/GetRepositoryItemResponseTestData.xml"));
        InputSource expectedResult = new InputSource(fis);
        DifferenceListener differenceListener = new IgnoreTextAndAttributeValuesDifferenceListener();
        Diff diff = new Diff(expectedResult, marshallingResult);
        diff.overrideDifferenceListener(differenceListener);
        assertTrue("Marshalled GetRepositoryItemResponse matches expected XML " + diff, diff.similar());
    }
