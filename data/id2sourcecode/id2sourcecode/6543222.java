    public JCompilationUnit createCompilationUnitForURI(String uri, String encoding) {
        try {
            URL url = null;
            try {
                url = new URL(uri);
            } catch (MalformedURLException exception) {
                url = new URL("file:" + uri);
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
            byte[] input = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(input);
            bufferedInputStream.close();
            return getControlModel().getFacadeHelper().createCompilationUnit(url.toString(), encoding == null ? new String(input) : new String(input, encoding));
        } catch (IOException exception) {
        }
        return null;
    }
