    public static Buffer runPE(DocumentBuilder context, URL url) throws IOException {
        return runMagic(MAGIC_PARAMETER, context, url, new Position(url), new InputStreamReader(url.openStream()), false);
    }
