    public PMIXMLRepository(String url) throws MalformedURLException, IOException {
        if (url == null || !url.startsWith(PMI_XML_PROTOCOL + ":")) {
            throw new MalformedURLException("The Repository URL should start with " + PMI_XML_PROTOCOL + ", but " + url + " was found");
        }
        url = url.substring(PMI_XML_PROTOCOL.length() + 1);
        try {
            populateRepository(this, new URL(url).openStream(), CustomisePERMIS.getAttributeCertificateAttribute());
        } catch (IOException ioe) {
            throw ioe;
        }
    }
