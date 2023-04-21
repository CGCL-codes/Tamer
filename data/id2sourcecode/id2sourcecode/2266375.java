    public AxisService getAxisService(String wsdlURI) throws IOException {
        URL url;
        if (wsdlURI.indexOf("://") == -1) {
            url = new URL("file", "", wsdlURI);
        } else {
            url = new URL(wsdlURI);
        }
        WSDL11ToAxisServiceBuilder builder = new WSDL11ToAxisServiceBuilder(url.openConnection().getInputStream());
        builder.setDocumentBaseUri(url.toString());
        builder.setBaseUri(getBaseUri(wsdlURI));
        builder.setCodegen(true);
        return builder.populateService();
    }
