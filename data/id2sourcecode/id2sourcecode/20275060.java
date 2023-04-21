    protected void rewrite(URLConnection connection, HttpServletRequest req, HttpServletResponse resp, URLRewriter rewriter) throws ServletException, IOException {
        InputStream in = new BufferedInputStream(connection.getInputStream());
        String encoding = connection.getContentEncoding();
        if (encoding != null) {
            if (encoding.endsWith("gzip")) in = new GZIPInputStream(in); else if (encoding.endsWith("deflate")) in = new InflaterInputStream(in);
        }
        InputStreamReader reader = CharacterEncodingDetector.getReader(in, null, 5000);
        try {
            resp.setContentType("text/html; charset=" + reader.getEncoding());
            Parser parser = new KanjiParser(dictionaries, null);
            parser.setIgnoreNewlines(true);
            HTMLAnnotator annotator = new HTMLAnnotator(parser);
            annotator.annotate(rewriter.getDocumentBase(), reader, resp.getWriter(), rewriter);
        } finally {
            in.close();
        }
    }
