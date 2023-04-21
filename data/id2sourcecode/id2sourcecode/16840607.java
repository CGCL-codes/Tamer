    public static File getExternalLocalFile(String url) throws IOException, MalformedURLException {
        File resources = createTempFolder();
        InputStream in = new URL(url).openStream();
        File temp = new File(resources.toString() + "/local.tmp");
        copyLocalFileto(temp, in);
        return temp;
    }
