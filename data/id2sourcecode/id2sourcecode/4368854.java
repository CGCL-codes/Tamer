    private void saveToFile(File fFile) throws GUIReaderException, GUIWriterException {
        ConfigProperties cp = new ConfigProperties();
        if (readList(cp)) _xmlWriter.writeOutput(cp, fFile);
    }
