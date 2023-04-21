    public Source resolve(String href, String base) throws TransformerException {
        Source result = null;
        try {
            if (this.resolver != null) {
                result = this.resolver.resolve(href, base);
                if (result != null) {
                    return result;
                }
            }
            URI uri = new URI(href);
            if (!uri.isAbsolute()) {
                System.out.println(base);
                URI baseURI = new URI(base);
                uri = baseURI.resolve(uri);
            }
            if (uri.getScheme().equals("chrome")) {
                result = new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream(uri.getPath().substring(1)));
            } else if (uri.getScheme().equals("file")) {
                result = new StreamSource(new FileInputStream(uri.getPath()));
            } else if (uri.getScheme().equals("http")) {
                URL url = new URL(uri.toString());
                result = new StreamSource(url.openStream());
            } else {
                throw new TransformerException("Scheme not recognized: " + uri.getScheme());
            }
            result.setSystemId(uri.toString());
        } catch (Exception e) {
            if (e instanceof TransformerException) {
                throw (TransformerException) e;
            } else {
                throw new TransformerException(e);
            }
        }
        return result;
    }
