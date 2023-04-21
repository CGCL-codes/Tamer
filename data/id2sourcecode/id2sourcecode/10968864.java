    @Override
    public OggSoundContainer loadSound(URL url) throws IOException {
        return (loadSound(url.openStream()));
    }
