    public String fetch(String url, String cache, boolean force) throws IOException {
        if (url == null) {
            throw new IllegalArgumentException("url cannot be null");
        }
        if (cache == null) {
            throw new IllegalArgumentException("cache cannot be null");
        }
        try {
            Reader is = URLFetcher.fetch(url, force);
            OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(cache), "utf-8");
            char chars[] = new char[200];
            int readCount = 0;
            while ((readCount = is.read(chars)) > 0) {
                os.write(chars, 0, readCount);
            }
            is.close();
            os.close();
        } catch (MalformedURLException e) {
            logger.error("Error in URL", e);
        }
        return cache;
    }
