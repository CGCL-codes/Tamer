    public Scene load(URL url) throws FileNotFoundException, IncorrectFormatException, ParsingErrorException {
        try {
            InputStreamReader in = new InputStreamReader(url.openStream());
            return load(in);
        } catch (IOException exp) {
            Log.err("IO Error : " + exp.getMessage());
            Raddoom.exit(Raddoom.EXIT_CODE_ERROR_LOADING_MAPFILE);
        }
        return null;
    }
