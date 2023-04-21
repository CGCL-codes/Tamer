    public FormBase(URL url, ErrorHandler eh, boolean editable) throws IOException, SAXException {
        this(url.openStream(), eh, editable);
    }
