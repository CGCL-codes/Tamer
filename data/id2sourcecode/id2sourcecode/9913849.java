    public void writeToXmlFile() {
        if (doc != null) {
            writeToXmlFile(tmpXmlFile);
        } else try {
            FileUtils.getEmptyXmlFile(tmpXmlFile);
        } catch (Exception e1) {
            log.log(Level.WARNING, "exception while initializing xml file");
        }
        try {
            FileUtils.copyFile(tmpXmlFile, xmlFile);
        } catch (IOException e) {
            log.log(Level.WARNING, "Fail to write to: " + xmlFile.getPath(), e);
        }
    }
