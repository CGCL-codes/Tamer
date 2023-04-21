    public static MfiFileFormat getMfiFileFormat(URL url) throws InvalidMfiDataException, IOException {
        return getMfiFileFormat(new BufferedInputStream(url.openStream()));
    }
