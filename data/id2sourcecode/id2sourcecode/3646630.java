    public Reader openURL(URL url) throws IOException {
        URLConnection uc = url.openConnection();
        String encoding = uc.getContentEncoding();
        InputStream in = uc.getInputStream();
        if (progressListener != null) {
            progressListener.setObjective("Loading: " + url);
            in = new ProgressInputStream(in, progressListener, uc.getContentLength());
        }
        Reader reader;
        try {
            if (encoding != null) {
                reader = new InputStreamReader(in, encoding);
            } else {
                reader = new InputStreamReader(in);
            }
        } catch (UnsupportedEncodingException ex) {
            reader = new InputStreamReader(in);
        }
        return reader;
    }
