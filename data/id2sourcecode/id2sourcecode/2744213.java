    protected XMLDataInputStreamCreator(String xmlFile) throws OdaException {
        URL url = null;
        try {
            File f = new File(xmlFile);
            if (f.exists()) url = f.toURL();
            this.url = url;
        } catch (IOException e) {
        }
        try {
            if (url == null) {
                url = new URL(xmlFile);
                this.createTemporaryFile(url.openStream());
            }
        } catch (MalformedURLException e) {
            throw new OdaException(e.getLocalizedMessage());
        } catch (IOException e) {
            throw new OdaException(e.getLocalizedMessage());
        }
    }
