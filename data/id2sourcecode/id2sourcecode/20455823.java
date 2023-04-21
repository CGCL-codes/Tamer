    public static List<Channel> getChannelsFromFile(String fileName) throws IOException, SAXException {
        FileReader fr = new FileReader(fileName);
        channels = new ArrayList<Channel>();
        XMLReader xr = XMLReaderFactory.createXMLReader();
        ImportExport handler = new ImportExport();
        xr.setContentHandler(handler);
        xr.setErrorHandler(handler);
        xr.parse(new InputSource(fr));
        return channels;
    }
