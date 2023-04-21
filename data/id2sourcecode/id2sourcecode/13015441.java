    public void read(Model model, String url) {
        try {
            read(model, new InputStreamReader(((new URL(url))).openStream()), url);
        } catch (Exception e) {
            throw new JenaException(e);
        } finally {
            if (errCount != 0) {
                throw new SyntaxError("unknown");
            }
        }
    }
