    public static XMLConfigurator loadFromClassPath(URL url) throws IOException {
        DOMRetriever xmlDoc = newInstance(url.openStream());
        return newInstance(xmlDoc);
    }
