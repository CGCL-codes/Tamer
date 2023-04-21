    public static void copy(File src, File trg) throws MalformedURLException, IOException, DocumentException {
        write(trg, read(src));
    }
