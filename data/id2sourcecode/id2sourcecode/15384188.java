    private void init(String fileName) throws IOException {
        file = new File(fileName);
        if (file != null && !file.exists()) {
            URL url = this.getClass().getResource(fileName);
            if (url == null && config != null) {
                url = config.getApplication().getClass().getResource(fileName);
            }
            if (url == null) {
                throw new IOException("Annotation structure file '" + fileName + "' not found.");
            }
            this.loadFromStream(url.openStream());
        } else {
            this.loadFromFile(file);
        }
        loadInformation();
    }
