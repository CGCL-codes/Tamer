    private static Representation getLocalImage(String sourceURL) throws IOException {
        URL url = new URL(sourceURL);
        final SeekableInputStream sis;
        if (url.getProtocol().equals("file")) {
            sis = new DefaultSeekableInputStream(new File(url.getFile()));
        } else {
            sis = new DefaultSeekableInputStream(null, url.openStream(), false);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Loading local file: " + url);
        }
        final String mimeType = DISCOVERER.discoverMimeType(sis);
        CachedHttpContentStateBuilder builder = new CachedHttpContentStateBuilder();
        final Map responses = new HashMap();
        Date now = new Date();
        DateFormat format = DateFormats.RFC_1123_GMT.create();
        responses.put("date", format.format(now));
        responses.put("cache-control", "max-age=" + LOCAL_FILE_MAX_AGE);
        responses.put("expires", format.format(new Date(now.getTime() + LOCAL_FILE_MAX_AGE * 1000)));
        HttpResponseHeaderAccessor accessor = new HttpResponseHeaderAccessor() {

            public String getProtocol() {
                return "HTTP";
            }

            public String getResponseHeaderValue(String s) {
                s = s.toLowerCase(Locale.ENGLISH);
                return (String) responses.get(s);
            }
        };
        builder.setMethodAccessor(accessor);
        Time t = Time.inMilliSeconds(System.currentTimeMillis());
        builder.setRequestTime(t);
        builder.setResponseTime(t);
        final CachedHttpContentState state = builder.build();
        return new Representation() {

            public void close() {
                try {
                    sis.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public CachedHttpContentState getCacheInfo() {
                return state;
            }

            public String getFileType() {
                return mimeType;
            }

            public SeekableInputStream getSeekableInputStream() {
                return sis;
            }
        };
    }
