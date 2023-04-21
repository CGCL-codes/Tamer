    private void initializeForParse() throws IOException {
        File fileToParse = new File(fileToParseName);
        if (!fileToParse.exists()) {
            RuntimeException ex = new RuntimeException("DataFileParser.parseFile: Error parsing file (" + fileToParseName + "), " + "the file does not exist.");
            msgCenter.error(ex.getMessage(), ex);
            throw ex;
        }
        FileChannel channel = new FileInputStream(fileToParse).getChannel();
        scanner = new RandomAccessScanner(channel);
        Element rootElement = descriptorDocument.getRootElement();
        if (rootElement.getName() != DataFileElements.FILE_DEF.getTag()) {
            RuntimeException ex = new RuntimeException("DataFileParser.parseFile: Error parsing file (" + fileToParseName + "), " + "the root node's name wasn't '" + DataFileElements.FILE_DEF.getTag() + "'.");
            msgCenter.error(ex.getMessage(), ex);
            throw ex;
        }
        outputBuilder.initialize();
    }
