    public static InputStream getResource(String resource, ServletContext context) throws Exception {
        if (resource == null) {
            return null;
        }
        InputStream stream = null;
        if (resource.trim().startsWith("/")) {
            logger.info("Get Local Resource: " + resource.trim());
            URL xslURL = context.getResource(resource.trim());
            String resourceURI = xslURL.toString();
            logger.info("Resolved Local Resource: " + resourceURI);
            stream = xslURL.openStream();
        } else {
            logger.info("Get External XSL Resource: " + resource.trim());
            URL url = new URL(resource.trim());
            String resourceURI = url.toString();
            logger.info("Resolved External XSL Resource: " + resourceURI);
            stream = url.openStream();
        }
        logger.info("Got Resource Stream successfully ");
        return stream;
    }
