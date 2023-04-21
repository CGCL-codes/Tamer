    public static void format(File f) throws MalformedURLException, IOException, DocumentException {
        write(f, read(f));
    }
