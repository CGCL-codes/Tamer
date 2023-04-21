    public MfiFileFormat getMfiFileFormat(URL url) throws InvalidMfiDataException, IOException {
        InputStream is = new BufferedInputStream(url.openStream());
        return VaviMfiFileFormat.readFrom(is);
    }
