    public void loadXmlFromFile(String filename, CMessageWriter writer) {
        loadXml(CFileHelper.readFile(filename), writer);
        this.cacheService.clearCache();
        writer.message("Finished loading resources from file: " + filename);
    }
