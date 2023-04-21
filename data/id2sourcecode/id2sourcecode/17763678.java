    public ParseCpsl(URL url, String encoding, HashMap existingMacros, HashMap existingTemplates) throws IOException {
        this(new BomStrippingInputStreamReader(url.openStream(), encoding), existingMacros, existingTemplates);
        baseURL = url;
        this.encoding = encoding;
    }
