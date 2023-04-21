    public static InputStream readUrl(String url) throws IOException {
        int i = 0;
        while (true) {
            try {
                return new URL(url).openStream();
            } catch (IOException e) {
                if (i++ >= MAX_RETRY) throw e;
            }
        }
    }
