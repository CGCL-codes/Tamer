    public ParticleSystem load(URL url) throws IOException, DataFormatException {
        InputStream stream = url.openStream();
        ParticleSystem ps = load(stream);
        stream.close();
        return ps;
    }
