    public void loadDescription() throws SAXException, IOException {
        URLConnection urlConn = new URL(getLocation()).openConnection();
        urlConn.setReadTimeout(HTTP_RECEIVE_TIMEOUT);
        XMLReader parser = XMLReaderFactory.createXMLReader();
        parser.setContentHandler(new GatewayDeviceHandler(this));
        parser.parse(new InputSource(urlConn.getInputStream()));
        String ipConDescURL;
        if (urlBase != null && urlBase.trim().length() > 0) {
            ipConDescURL = urlBase;
        } else {
            ipConDescURL = location;
        }
        int lastSlashIndex = ipConDescURL.indexOf('/', 7);
        if (lastSlashIndex > 0) {
            ipConDescURL = ipConDescURL.substring(0, lastSlashIndex);
        }
        sCPDURL = copyOrCatUrl(ipConDescURL, sCPDURL);
        controlURL = copyOrCatUrl(ipConDescURL, controlURL);
        controlURLCIF = copyOrCatUrl(ipConDescURL, controlURLCIF);
        presentationURL = copyOrCatUrl(ipConDescURL, presentationURL);
    }
