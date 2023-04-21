    public void refresh(boolean fromOriginal) throws Exception {
        if (fromOriginal) {
            if (!xmlFile.exists()) {
                FileUtils.getEmptyXmlFile(xmlFile);
            }
            FileUtils.copyFile(xmlFile, tmpXmlFile);
        }
        doc = FileUtils.readDocumentFromFile(tmpXmlFile);
        root = new ElementNode(doc.getDocumentElement(), null, viewType);
    }
