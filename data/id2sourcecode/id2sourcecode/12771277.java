    public static IMemento loadMemento(URL url) throws IOException {
        return XMLMemento.createReadRoot(new InputStreamReader(url.openStream()));
    }
