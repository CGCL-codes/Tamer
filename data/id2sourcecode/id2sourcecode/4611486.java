    public static Element getDocRoot(String urlString, DocType... docTypes) {
        try {
            URL url = new URL(urlString);
            InputStream stream = url.openStream();
            try {
                Element root = getRoot(stream, docTypes);
                return root;
            } finally {
                stream.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
