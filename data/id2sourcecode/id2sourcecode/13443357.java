    public boolean parse(final URL url, final Component parentComponent) throws InterruptedIOException, IOException {
        InputStream stream = null;
        try {
            stream = url.openStream();
        } catch (IOException e) {
            if (stream != null) {
                stream.close();
            }
            throw e;
        }
        stream = new ProgressMonitorInputStream(parentComponent, "analyzing " + url.toString(), stream);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        boolean isAscii = false;
        try {
            isAscii = parse(reader);
        } finally {
            reader.close();
        }
        if (isAscii) {
            try {
                stream = url.openStream();
            } catch (IOException e) {
                stream.close();
                throw e;
            }
            stream = new ProgressMonitorInputStream(parentComponent, "parsing " + url.toString(), stream);
            reader = new BufferedReader(new InputStreamReader(stream));
            try {
                configureTokenizer(reader);
            } catch (IOException e) {
                reader.close();
                throw e;
            }
            itsReader = reader;
        }
        return isAscii;
    }
