    private static String getTextFromURLStream(URL url, int startOffset, int endOffset, String charset) throws IOException {
        if (url == null) return null;
        if (startOffset > endOffset) throw new IOException();
        InputStream fis = url.openStream();
        InputStreamReader fisreader = charset == null ? new InputStreamReader(fis) : new InputStreamReader(fis, charset);
        int len = endOffset - startOffset;
        int bytesAlreadyRead = 0;
        char buffer[] = new char[len];
        int bytesToSkip = startOffset;
        long bytesSkipped = 0;
        do {
            bytesSkipped = fisreader.skip(bytesToSkip);
            bytesToSkip -= bytesSkipped;
        } while ((bytesToSkip > 0) && (bytesSkipped > 0));
        do {
            int count = fisreader.read(buffer, bytesAlreadyRead, len - bytesAlreadyRead);
            if (count < 0) {
                break;
            }
            bytesAlreadyRead += count;
        } while (bytesAlreadyRead < len);
        fisreader.close();
        return new String(buffer);
    }
