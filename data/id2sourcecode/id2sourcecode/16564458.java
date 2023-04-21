    public void testWriteMpeg7() throws ProcessingException, MalformedURLException {
        IExtractorConfig config = Mpeg7ConfigUtil.createConfig(Mpeg7ExtractorEnum.values());
        Mpeg7ExtractorInputReader reader = new Mpeg7ExtractorInputReader();
        reader.setConfig(config);
        Mpeg7Factory.createMpeg7Writer().write((new File("./target/test-classes/text1.wav")).toURI().toURL(), (new File("../data/text1.mpeg7.xml")), reader.getConfig());
    }
