    public List<ProvaRule> parse(ProvaKnowledgeBase kb, ProvaResultSet resultSet, String filename) throws IOException, ProvaParsingException {
        File file = new File(filename);
        BufferedReader in;
        if (!file.exists() || !file.canRead()) {
            try {
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
                in = new BufferedReader(new InputStreamReader(is));
            } catch (Exception e) {
                try {
                    URL url = new URL(filename);
                    in = new BufferedReader(new InputStreamReader(url.openStream()));
                } catch (Exception ex) {
                    throw new IOException("Cannot read from " + filename);
                }
            }
        } else {
            FileReader fr = new FileReader(file);
            in = new BufferedReader(fr);
        }
        return parse(kb, resultSet, in);
    }
