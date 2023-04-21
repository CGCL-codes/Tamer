    @Override
    protected void setUp() throws Exception {
        XMLUnit.setControlParser("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
        XMLUnit.setTestParser("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
        XMLUnit.setSAXParserFactory("org.apache.xerces.jaxp.SAXParserFactoryImpl");
        XMLUnit.setTransformerFactory("org.apache.xalan.processor.TransformerFactoryImpl");
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setNormalizeWhitespace(true);
        x3dc = new X3dCanonicalizer(new String[] { testFilesDir + "HelloWorld.x3d" });
        try {
            raf = new RandomAccessFile(testFilesDir + "HelloWorld.x3d", "rwd");
            fc = raf.getChannel();
            bb = ByteBuffer.allocate((int) fc.size());
            fc.read(bb);
            raf.close();
        } catch (IOException ioe) {
            log.info("scene is read-only!  Can not process.");
        }
        bb.flip();
        sc = new String(bb.array());
    }
