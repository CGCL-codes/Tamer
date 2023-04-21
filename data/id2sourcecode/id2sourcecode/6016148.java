    public static Schema loadSchema(URL url, Log log) throws Exception {
        return loadSchema(url.openStream(), log);
    }
