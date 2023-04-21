    public void getInfo() throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        URLConnection urlConn = new URL(location).openConnection();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document d = builder.parse(urlConn.getInputStream());
        XPath xPath = XPathFactory.newInstance().newXPath();
        Element e = (Element) (xPath.evaluate("//URLBase", d, XPathConstants.NODE));
        urlBase = e.getTextContent();
        if (urlBase.endsWith("/")) urlBase = urlBase.substring(0, urlBase.length() - 1);
        e = (Element) (xPath.evaluate("//deviceType[text()='urn:schemas-upnp-org:device:InternetGatewayDevice:1']/../friendlyName", d, XPathConstants.NODE));
        friendlyName = e.getTextContent();
        e = (Element) (xPath.evaluate("//deviceType[text()='urn:schemas-upnp-org:device:WANConnectionDevice:1']/../serviceList/service/serviceType[text()='urn:schemas-upnp-org:service:WANIPConnection:1' or text()='urn:schemas-upnp-org:service:WANPPPConnection:1']", d, XPathConstants.NODE));
        serviceType = e.getTextContent();
        e = (Element) (xPath.evaluate("../controlURL", e, XPathConstants.NODE));
        controlURL = formatURL(e.getTextContent());
        if (!controlURL.startsWith("/")) controlURL = "/" + controlURL;
    }
