    public static void download(URL url, File dest) throws IOException {
        download(url.openStream(), dest);
    }
