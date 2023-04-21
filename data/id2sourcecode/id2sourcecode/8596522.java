    @Override
    public DaeModel loadModel(URL url, String skin) throws IOException, IncorrectFormatException, ParsingErrorException {
        NullArgumentException.check(url);
        boolean baseURLWasNull = setBaseURLFromModelURL(url);
        DaeModel scene = loadModel(url.openStream());
        if (baseURLWasNull) {
            popBaseURL();
        }
        return (scene);
    }
