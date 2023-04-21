    private BufferedReader openReader(final Object source) throws IOException {
        BufferedReader reader;
        if (source instanceof File) {
            File file = (File) source;
            log.info("Using source file: " + file);
            reader = new BufferedReader(new FileReader(file));
        } else if (source instanceof URL) {
            URL url = (URL) source;
            log.info("Using source URL: " + url);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
        } else {
            String tmp = String.valueOf(source);
            try {
                URL url = new URL(tmp);
                log.info("Using source URL: " + url);
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
            } catch (MalformedURLException ignore) {
                File file = new File(tmp);
                log.info("Using source file: " + file);
                reader = new BufferedReader(new FileReader(tmp));
            }
        }
        return reader;
    }
