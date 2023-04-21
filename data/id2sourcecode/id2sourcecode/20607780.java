    public static DBDData resolveDBDasURL(DBDData data, java.net.URL url) {
        if (data == null) data = new DBDData();
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
            } catch (Exception e) {
                data = null;
            } finally {
                System.gc();
            }
        } else return null;
        return data;
    }
