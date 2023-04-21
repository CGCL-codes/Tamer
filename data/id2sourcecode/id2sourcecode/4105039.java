    public void write(URL url, BufferedWriter writer) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        write(reader, writer);
    }
