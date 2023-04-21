    public Element getElement() {
        try {
            InputStream stream = url.openStream();
            Element element = elementParser.parse(stream);
            return element;
        } catch (IOException e) {
            throw new NgreaseException(e);
        }
    }
