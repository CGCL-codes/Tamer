    public boolean parse(final URL url) throws IOException {
        InputStream stream = null;
        try {
            stream = url.openStream();
        } catch (IOException e) {
            if (stream != null) {
                stream.close();
            }
            throw e;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        boolean isAscii = false;
        try {
            isAscii = parse(reader);
        } catch (InterruptedIOException e) {
            e.printStackTrace();
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
