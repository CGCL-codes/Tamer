    public UrlRepo(URL url, ConfigFormat format) {
        Validate.notNull(url, "URL cannot be null");
        this.url = url;
        source = new AbstractFormattedSource(format) {

            {
                setParent(UrlRepo.this);
            }

            public BufferedReader newReader() {
                try {
                    return buffer(UrlRepo.this.url.openStream());
                } catch (IOException e) {
                    throw new CongaRuntimeException(e);
                }
            }
        };
    }
