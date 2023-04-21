    @Override
    public Coordinate getCoordinate(final String address) {
        String strippedAddress = null;
        try {
            strippedAddress = StringUtils.trim(address);
            strippedAddress = URLEncoder.encode(strippedAddress, IConstants.ENCODING);
            String uri = getUri(strippedAddress);
            URL url = new URL(uri);
            String xml = FileUtilities.getContents(url.openStream(), Integer.MAX_VALUE).toString();
            InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
            Element rootElement = XmlUtilities.getDocument(inputStream, IConstants.ENCODING).getRootElement();
            Element element = XmlUtilities.getElement(rootElement, IConstants.LOCATION);
            Element latitudeElement = XmlUtilities.getElement(element, IConstants.LAT);
            Element longitudeElement = XmlUtilities.getElement(element, IConstants.LNG);
            double lat = Double.parseDouble(latitudeElement.getText());
            double lng = Double.parseDouble(longitudeElement.getText());
            return new Coordinate(lat, lng, strippedAddress);
        } catch (Exception e) {
            LOGGER.error("Exception accessing the GeoCode url : " + searchUrl + ", " + strippedAddress, e);
        }
        return null;
    }
