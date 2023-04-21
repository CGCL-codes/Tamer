    private void readUrl() {
        URL url;
        try {
            url = new URL(myIDFile.getFilePath());
        } catch (MalformedURLException ex) {
            this.setErrorIdent();
            this.setIdentificationWarning("URL is malformed");
            return;
        }
        try {
            readStream(url.openStream());
        } catch (IOException ex) {
            this.setErrorIdent();
            this.setIdentificationWarning("URL could not be read");
        }
    }
