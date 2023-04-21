    private synchronized Central callREST(String path) throws CentralException {
        Central central = null;
        String urlString = baseURL + "/REST/" + path;
        if (gaSession != null) {
            urlString += "?GAsession=" + gaSession;
        }
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
        }
        SAXSource source;
        try {
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            if (connection.getResponseCode() == 401) {
                throw new CentralException("You are not authorized to view this resource");
            }
            source = new SAXSource(xmlReader, new InputSource(connection.getInputStream()));
        } catch (IOException e) {
            throw new CentralException("Error connecting to NEEScentral", e);
        }
        JAXBElement centralElement;
        try {
            centralElement = (JAXBElement) unmarshaller.unmarshal(source);
        } catch (JAXBException e) {
            throw new CentralException("NEEScentral returned an invalid response", e);
        }
        central = (Central) centralElement.getValue();
        return central;
    }
