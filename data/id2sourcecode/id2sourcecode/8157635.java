    public void load(String fileName) throws IOException {
        Set<String> prefixes = new HashSet<String>();
        BufferedReader f;
        try {
            URL url = new URL(fileName);
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            f = new BufferedReader(new InputStreamReader(in));
        } catch (MalformedURLException e) {
            f = new BufferedReader(new FileReader(fileName));
        }
        int lines = 0;
        parseStream(f, "", 0, lines, prefixes);
        f.close();
    }
