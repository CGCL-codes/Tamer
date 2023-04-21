    @Test
    public void apiVerification() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("http://maps.googleapis.com/maps/api/geocode/xml");
        builder.append("?");
        builder.append(GoogleGeocoder.ADDRESS);
        builder.append("=");
        builder.append("9a%20avenue%20road,%20cape%20town,%20south%20africa");
        builder.append("&");
        builder.append(GoogleGeocoder.SENSOR);
        builder.append("=");
        builder.append("true");
        URL url = new URL(builder.toString());
        logger.info("Url : " + builder.toString());
        String content = FileUtilities.getContents(url.openStream(), Integer.MAX_VALUE).toString();
        logger.info(content);
        assertNotNull(content);
    }
