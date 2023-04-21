    private void geocodeStop(String stopname, float[] coordinates) throws MalformedURLException, UnsupportedEncodingException, XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        final String geocoderPrefix = "http://maps.google.com/maps/api/geocode/xml?address=";
        final String geocoderPostfix = "&sensor=false";
        if (stopname == null || coordinates == null || coordinates.length != 2) return;
        String geoaddress = geocoderPrefix + stopname + geocoderPostfix;
        System.out.println("	Trying: " + geoaddress);
        URL url = new URL(geocoderPrefix + URLEncoder.encode(stopname, "UTF-8") + geocoderPostfix);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        InputSource inputStream = new InputSource(conn.getInputStream());
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
        XPath xp = XPathFactory.newInstance().newXPath();
        NodeList geocodedNodes = (NodeList) xp.evaluate("/GeocodeResponse/result[1]/geometry/location/*", doc, XPathConstants.NODESET);
        float lat = -999999;
        float lon = -999999;
        Node node;
        for (int i = 0; i < geocodedNodes.getLength(); i++) {
            node = geocodedNodes.item(i);
            if ("lat".equals(node.getNodeName())) lat = Float.parseFloat(node.getTextContent());
            if ("lng".equals(node.getNodeName())) lon = Float.parseFloat(node.getTextContent());
        }
        coordinates[0] = lat;
        coordinates[1] = lon;
    }
