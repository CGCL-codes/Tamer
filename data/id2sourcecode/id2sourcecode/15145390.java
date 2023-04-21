    public void doTestCondition(InputStream input, InputStream expected) throws Exception {
        CharArrayWriter outputWriter = new CharArrayWriter();
        OutputFormat format = new OutputFormat();
        format.setPreserveSpace(true);
        format.setOmitXMLDeclaration(true);
        XMLSerializer serializer = new XMLSerializer(format);
        serializer.setOutputCharStream(outputWriter);
        ContentHandler sHandler = serializer.asContentHandler();
        XMLHandlerAdapter adapter = new XMLHandlerAdapter();
        adapter.setContentHandler(sHandler);
        XMLReader parser = XMLReaderFactory.createXMLReader(false);
        XMLFilterImpl xmlFilter = new XMLFilterImpl(parser);
        HTMLResponseConditioner conditioner = new HTMLResponseConditioner(xmlFilter);
        XMLProcessImpl xmlProcess = new XMLProcessImpl() {

            public void setDocumentLocator(Locator locator) {
                XMLProcess consumer = getConsumerProcess();
                if (null != consumer) {
                    consumer.setDocumentLocator(locator);
                }
            }

            public void startDocument() throws SAXException {
                XMLProcess consumer = getConsumerProcess();
                if (null != consumer) {
                    consumer.startDocument();
                }
            }

            public void endDocument() throws SAXException {
                XMLProcess consumer = getConsumerProcess();
                if (null != consumer) {
                    consumer.endDocument();
                }
            }

            public void startPrefixMapping(String prefix, String uri) throws SAXException {
                XMLProcess consumer = getConsumerProcess();
                if (null != consumer) {
                    consumer.startPrefixMapping(prefix, uri);
                }
            }

            public void endPrefixMapping(String prefix) throws SAXException {
                XMLProcess consumer = getConsumerProcess();
                if (null != consumer) {
                    consumer.endPrefixMapping(prefix);
                }
            }
        };
        xmlProcess.setNextProcess(adapter);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int readBytes = input.read(buffer);
        while (readBytes != -1) {
            out.write(buffer, 0, readBytes);
            readBytes = input.read(buffer);
        }
        InputSource inputSource = new InputSource(new ByteArrayInputStream(out.toByteArray()));
        conditioner.condition(inputSource, xmlProcess);
        outputWriter.flush();
        char[] outputCharacters = outputWriter.toCharArray();
        String charsetName = "ISO-8859-1";
        XMLAssert.assertXMLEqual(new InputStreamReader(expected, charsetName), new CharArrayReader(outputCharacters));
    }
