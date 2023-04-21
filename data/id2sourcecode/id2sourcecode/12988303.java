    public static String getCityForLocation(double latitude, double longtitude) {
        String city = null;
        String query = "http://ws.geonames.org/findNearbyPlaceName?lat=" + latitude + "&lng=" + longtitude;
        DocumentBuilder builder;
        try {
            Activator.log("Querying " + query);
            System.out.println("Querying " + query);
            URL url = new URL(query);
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(url.openStream());
            XPath xpath = XPathFactory.newInstance().newXPath();
            city = (String) xpath.evaluate("//geonames/geoname/name/text()", doc, XPathConstants.STRING);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Activator.log("CityForLocation: " + city);
        System.out.println("CityForLocation: " + city);
        return city;
    }
