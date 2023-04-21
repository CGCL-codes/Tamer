    private boolean validateXerces1(URL url, DocumentBuilder docBuilder) throws Exception {
        InputStream in = url.openStream();
        docBuilder.parse(in);
        in.close();
        return true;
    }
