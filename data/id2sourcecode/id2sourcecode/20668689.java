    private void fetchDataIfNeeded(String conceptUri) {
        if (!fetchedUris.contains(conceptUri)) {
            logger.info("Fetching data for: " + conceptUri);
            try {
                URL url = new URL(conceptUri);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setInstanceFollowRedirects(true);
                connection.setRequestProperty("Accept", "application/rdf+xml");
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK && connection.getContentType().contains("application/rdf+xml")) {
                    InputStream is = connection.getInputStream();
                    getOntModel().read(is, conceptUri);
                    is.close();
                    fetchedUris.add(conceptUri);
                } else {
                    logger.error("Unable to get a representation of the resource: " + connection.getResponseCode() + " => " + connection.getContentType());
                    throw new RuntimeException("Unable to get a representation of the resource");
                }
            } catch (Exception e) {
                logger.error("Unable to fetch data for concept " + conceptUri, e);
                throw new RuntimeException(e);
            }
        }
    }
