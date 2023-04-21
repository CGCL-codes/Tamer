    @Override
    public BSPScene loadScene(URL url) throws IOException, IncorrectFormatException, ParsingErrorException {
        final boolean baseURLWasNull = setBaseURLFromModelURL(url);
        BSPScene scene;
        if (url.getProtocol().equals("file")) {
            try {
                final boolean basePathWasNull = (getBasePath() == null);
                setBasePath(new File(getBaseURL().toURI()).getAbsolutePath());
                scene = loadScene(new File(url.toURI()).getAbsolutePath());
                if (basePathWasNull) popBasePath();
            } catch (URISyntaxException e) {
                final IOException ioe = new IOException(e.getMessage());
                ioe.initCause(e);
                throw (ioe);
            }
        } else {
            scene = loadScene(url.openStream());
        }
        if (baseURLWasNull) {
            popBaseURL();
        }
        return (scene);
    }
