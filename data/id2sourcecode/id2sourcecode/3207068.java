    public int importFile(String file, String db) throws MalformedURLException, IOException {
        URL url = new URL(file);
        URLConnection conn = url.openConnection();
        InputStream in = conn.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = br.readLine();
        while (line != null) {
            Vector parts = StringUtils.split(line, " ");
            if (parts.size() == 4) parts.remove(3);
            line = StringUtils.join(parts, " ");
            add(db, line);
            line = br.readLine();
        }
        return 0;
    }
