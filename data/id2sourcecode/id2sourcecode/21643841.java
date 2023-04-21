    void copyXmlAndInitializeProps() throws Exception {
        File tempFile = File.createTempFile("sample-lucene", ".xml");
        ClassPathResource classPathResource = new ClassPathResource("lucene.xml");
        FileUtils.copyFile(classPathResource.getFile(), tempFile);
        properties = new Properties();
        properties.setProperty("mvn.lucene.configFileLocation", tempFile.getAbsolutePath());
    }
