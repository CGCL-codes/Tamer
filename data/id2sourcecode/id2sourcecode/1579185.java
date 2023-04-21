    public Scene load(URL url) throws FileNotFoundException, IncorrectFormatException, ParsingErrorException {
        BufferedReader reader;
        setBaseUrlFromUrl(url);
        try {
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
        fromUrl = true;
        return load(reader);
    }
