    public static DBData resolveDBasURL(java.net.URL url) throws Exception {
        DBData data = null;
        InputStream fi = null;
        EnhancedStreamTokenizer tokenizer = null;
        try {
            fi = url.openStream();
            tokenizer = new EnhancedStreamTokenizer(new BufferedReader(new InputStreamReader(fi)));
            initializeTokenizer(tokenizer);
        } catch (Exception e) {
            debug.finer("\nError occured while opening URL '" + url.toString() + "'");
            debug.finer(e.toString());
            return null;
        }
        if (tokenizer != null) {
            try {
            } finally {
                System.gc();
            }
        }
        return data;
    }
