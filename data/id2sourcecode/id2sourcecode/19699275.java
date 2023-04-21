    public void parse(CheckedURL pURL) throws IOException {
        final String mName = "parse(CheckedURL)";
        logger.finest(mName, "->", pURL.url);
        logger.fine(mName, "Open", pURL.url);
        InputStream stream = pURL.stream;
        if (stream == null) {
            try {
                stream = pURL.url.openStream();
            } catch (IOException e) {
                handleRefError(pURL.url, pURL.referencingURL, pURL.referencingPos, "Failed to open URL: " + e.getMessage());
                return;
            }
        }
        if (pURL.checkExistsOnly || pURL.isExtern) {
            stream.close();
        } else {
            BufferedInputStream bStream = new BufferedInputStream(stream, 4096);
            ParserDelegator parser = new ParserDelegator();
            HTMLEditorKit.ParserCallback callback = new URLChecker(pURL);
            parser.parse(new InputStreamReader(bStream), callback, false);
            bStream.close();
        }
        logger.finest(mName, "<-");
    }
