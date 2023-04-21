    public List<Publicacao> lerPublicacoes(String urlLates) throws IOException, SAXException {
        URL url = new URL(urlLates);
        InputStream in = url.openStream();
        return lerPublicacoes(in);
    }
