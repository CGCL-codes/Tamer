    public static String guessCharSet(URL url) throws IOException {
        return guessCharSet(url.openStream());
    }
