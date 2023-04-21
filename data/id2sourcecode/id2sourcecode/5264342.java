    @Override
    public AC3DScene loadScene(URL url) throws IOException, IncorrectFormatException, ParsingErrorException {
        boolean baseURLWasNull = setBaseURLFromModelURL(url);
        AC3DScene scene = loadScene(url.openStream());
        if (baseURLWasNull) {
            popBaseURL();
        }
        return (scene);
    }
