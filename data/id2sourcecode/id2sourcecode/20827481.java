    public SmafFileFormat getSmafFileFormat(URL url) throws InvalidSmafDataException, IOException {
        InputStream is = new BufferedInputStream(url.openStream());
        return SmafFileFormat.readFrom(is);
    }
