    public Collection<Place> findPlaces(final String aSearchExpression) {
        LinkedList<Place> retval = new LinkedList<Place>();
        try {
            URL url = new URL("http://www.frankieandshadow.com/osm/search.xml?find=" + URLEncoder.encode(aSearchExpression, "UTF-8"));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(url.openStream()));
            NodeList wayElements = document.getDocumentElement().getElementsByTagName("named");
            for (int i = 0; i < wayElements.getLength(); i++) {
                try {
                    Element wayElement = (Element) wayElements.item(i);
                    String type = wayElement.getAttribute("type");
                    if (type == null || !type.equalsIgnoreCase("way")) continue;
                    long id = Long.parseLong(wayElement.getAttribute("id"));
                    String name = wayElement.getAttribute(Tags.TAG_NAME);
                    double lat = Double.parseDouble(wayElement.getAttribute("lat"));
                    double lon = Double.parseDouble(wayElement.getAttribute("lon"));
                    retval.add(new WayReferencePlace(myMap, name, id, lat, lon));
                } catch (RuntimeException e) {
                    LOG.log(Level.SEVERE, "Exception while searching with the OpenStreetMap-Name_finder for '" + aSearchExpression + "' with result #" + i, e);
                }
            }
            return retval;
        } catch (UnknownHostException e) {
            LOG.log(Level.INFO, "We seem to have no internet while searching with the OpenStreetMap-Name_finder for '" + aSearchExpression + "'");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Exception while searching with the OpenStreetMap-Name_finder for '" + aSearchExpression + "'", e);
        }
        return retval;
    }
