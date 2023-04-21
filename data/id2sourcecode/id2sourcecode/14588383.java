    public static BufferedReader readerFromURL(String urlStr) {
        try {
            return asBufferedUTF8(new URL(urlStr).openStream());
        } catch (java.net.MalformedURLException e) {
            try {
                return asBufferedUTF8(new FileInputStream(urlStr));
            } catch (FileNotFoundException f) {
                throw new WrappedIOException(f);
            }
        } catch (IOException e) {
            throw new WrappedIOException(e);
        }
    }
