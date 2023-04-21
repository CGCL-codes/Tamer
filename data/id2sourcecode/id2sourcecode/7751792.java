    private InputStream checkUrlDataFormat(URL url) throws UniprotServiceException, IOException {
        if (url == null) {
            throw new IllegalArgumentException("URL must not be null.");
        }
        PushbackInputStream pis = new PushbackInputStream(url.openStream(), 5);
        byte[] b = new byte[CHAR_TO_READ];
        pis.read(b, 0, CHAR_TO_READ);
        if (b.length < CHAR_TO_READ) {
            throw new RuntimeException("Could not read the whole 5 bytes");
        }
        String fiveFirstChars = new String(b);
        if (!FIRST_LINE_FIRST_FIVE_CHARS.equals(fiveFirstChars)) {
            throw new UniprotServiceException("Invalid UniProt entry format. An entry is expected to start with :'" + FIRST_LINE_FIRST_FIVE_CHARS + "' and not '" + fiveFirstChars + "'.");
        }
        pis.unread(b);
        return pis;
    }
