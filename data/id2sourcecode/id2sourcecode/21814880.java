    public InputStream getBinaryFile(String filename) throws IOException {
        filename = fixURL(filename);
        String url = IOUtil.delimitURL(IOUtil.fixJarURL(urlBase_ + filename));
        System.err.println("Retrieving url " + url + " from server");
        InputStream retval = new BufferedInputStream(new URL(url).openStream());
        return retval;
    }
