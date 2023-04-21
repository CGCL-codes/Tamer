    public URLOutputAdapter(URL url) throws IOException {
        super(new BufferedWriter(new OutputStreamWriter(url.openConnection().getOutputStream())));
    }
