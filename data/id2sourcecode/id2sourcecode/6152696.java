    public GribTab(URL url) throws IOException {
        InputStream is = url.openStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        init(br);
        resource = url.toString();
    }
