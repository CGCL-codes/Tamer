    public void testTransform1() throws Exception {
        TolvenLogger.defaultInitialize();
        Transformer transformer = new Transformer(getClass().getResourceAsStream("CDAtoTRIM.xsl"));
        Reader reader = new InputStreamReader(getClass().getResourceAsStream("cda1.xml"));
        Writer writer = new StringWriter();
        transformer.transform(reader, writer);
        logger.info(writer.toString());
    }
