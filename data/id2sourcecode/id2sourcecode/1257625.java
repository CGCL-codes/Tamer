    public static InputStream getRawInputStream(String fileName) throws FileNotFoundException, IOException, NoClassDefFoundError {
        try {
            URL urlAttempt = new URL(fileName);
            PushbackInputStream test = new PushbackInputStream(urlAttempt.openStream());
            return (new BufferedInputStream(test));
        } catch (Exception e) {
            return (null);
        }
    }
