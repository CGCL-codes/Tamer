    public TBXFile(URL u, Configuration c) throws IOException, SAXException {
        if (c == null) throw new IllegalArgumentException("Configuration cannot be null");
        config = c;
        if (u == null) throw new IllegalArgumentException("URL argument cannot be null");
        url = u;
        resolver = new TBXResolver(url);
        tbxParser = new TBXParser(resolver, c);
        InputStream input = url.openStream();
        if (!input.markSupported()) input = new BufferedInputStream(input);
        InputStreamReader inread = new InputStreamReader(input, TBXResolver.getEncoding(input));
        reader = new BufferedReader(inread);
    }
