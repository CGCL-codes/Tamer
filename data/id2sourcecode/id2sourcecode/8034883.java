    public DTMDocument loadDocument(URL docurl, DynamicContext dynEnv) throws XQueryException {
        if (docurl == null) {
            throw new IllegalArgumentException("docurl is null");
        }
        final String urlStr = docurl.toString();
        final String unescaped = XMLUtils.unescapeXML(urlStr);
        if (!unescaped.equals(urlStr)) {
            try {
                docurl = new URL(unescaped);
            } catch (MalformedURLException e) {
                throw new IllegalStateException("failed to decode as URL: " + unescaped, e);
            }
        }
        final DTMDocument xqdoc;
        if (_sharedCache.containsKey(docurl)) {
            xqdoc = _sharedCache.get(docurl);
        } else {
            final InputStream is;
            boolean parseAsHtml = false;
            try {
                final URLConnection conn = docurl.openConnection();
                final String contentType = conn.getContentType();
                if (unescaped.endsWith(".html") || (contentType != null && contentType.contains("html"))) {
                    parseAsHtml = true;
                    try {
                        conn.setRequestProperty("User-agent", "Mozilla/5.0");
                    } catch (IllegalStateException ace) {
                        ;
                    }
                }
                is = conn.getInputStream();
            } catch (IOException e) {
                throw new DynamicError("Openning a document failed: " + unescaped, e);
            }
            final boolean resolveEntity = unescaped.startsWith("http");
            final DocumentTableModel dtm = new DocumentTableModel(parseAsHtml, resolveEntity);
            try {
                dtm.loadDocument(is, dynEnv);
            } catch (XQueryException e) {
                throw new DynamicError("loading a document failed: " + unescaped, e);
            }
            xqdoc = dtm.documentNode();
            xqdoc.setDocumentUri(unescaped);
            _sharedCache.put(docurl, xqdoc);
        }
        Map<String, String> nsmap = xqdoc.documentTable().getDeclaredNamespaces();
        NamespaceBinder nsResolver = dynEnv.getStaticContext().getStaticalyKnownNamespaces();
        nsResolver.declarePrefixs(nsmap);
        return xqdoc;
    }
