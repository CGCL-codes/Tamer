    public Reader getArquivo(URL url) throws Exception {
        return new InputStreamReader(url.openConnection().getInputStream());
    }
