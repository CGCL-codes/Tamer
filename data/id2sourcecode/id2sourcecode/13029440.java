    public Source resolve(String href, String base) throws TransformerException {
        URL url = clazz.getResource(href);
        if (url != null) try {
            return new StreamSource((InputStream) url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
