    private Iterable<String> getWSDLUrls() throws IOException, SAXException {
        URLConnection urlConnection = registryUrl.openConnection();
        urlConnection.addRequestProperty("accept", "application/xml");
        InputStream is = urlConnection.getInputStream();
        RegistryResponseParser parser = new RegistryResponseParser();
        parser.parse(is);
        return parser.getUrls();
    }
