    public static BufferedReader fetchBufferedReader(URL url) throws IOException {
        BufferedReader in = null;
        InputStream is = url.openStream();
        String name = url.toString();
        if (name.endsWith(".gz")) {
            in = new BufferedReader(new InputStreamReader(new GZIPInputStream(is)));
        } else if (name.endsWith(".zip")) {
            ZipInputStream zis = new ZipInputStream(is);
            zis.getNextEntry();
            in = new BufferedReader(new InputStreamReader(zis));
        } else in = new BufferedReader(new InputStreamReader(is));
        return in;
    }
