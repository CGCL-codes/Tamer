    public Process(URL url) throws IOException, XMLException {
        initContext();
        Reader in = new InputStreamReader(url.openStream(), getEncoding(null));
        readProcess(in);
        in.close();
    }
