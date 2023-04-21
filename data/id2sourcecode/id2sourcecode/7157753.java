    public Scene load(URL url) throws FileNotFoundException, IncorrectFormatException, ParsingErrorException {
        Reader reader;
        if (baseUrl == null) tokenHandler.setBaseUrl(url); else tokenHandler.setBaseUrl(baseUrl);
        try {
            reader = new InputStreamReader(url.openStream());
        } catch (IOException e) {
            throw new FileNotFoundException(e.getMessage());
        }
        return load(reader);
    }
