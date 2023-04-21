    public void loadDescription() throws WeUPnPException {
        URLConnection urlConn;
        try {
            urlConn = new URL(getLocation()).openConnection();
            XMLReader parser;
            parser = XMLReaderFactory.createXMLReader();
            parser.setContentHandler(new GatewayDeviceHandler(this));
            parser.parse(new InputSource(urlConn.getInputStream()));
            String ipConDescURL;
            if (uRLBase != null && uRLBase.trim().length() > 0) ipConDescURL = uRLBase; else ipConDescURL = location;
            final int lastSlashIndex = ipConDescURL.indexOf('/', 7);
            if (lastSlashIndex > 0) ipConDescURL = ipConDescURL.substring(0, lastSlashIndex);
            sCPDURL = copyOrCatUrl(ipConDescURL, sCPDURL);
            controlURL = copyOrCatUrl(ipConDescURL, controlURL);
            controlURLCIF = copyOrCatUrl(ipConDescURL, controlURLCIF);
        } catch (final MalformedURLException e) {
            throw new WeUPnPException("Could not load description", e);
        } catch (final IOException e) {
            throw new WeUPnPException("Could not load description", e);
        } catch (final SAXException e) {
            throw new WeUPnPException("Could not load description", e);
        }
    }
