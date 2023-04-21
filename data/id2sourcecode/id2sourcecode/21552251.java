    public InputStream createInputStream() throws IOException {
        InputStream urlInputStream = null;
        InputStream limitedInputStream = null;
        urlInputStream = this.url.openStream();
        urlInputStream.skip(this.position);
        limitedInputStream = new LimitingInputStream(urlInputStream, (int) this.length);
        return limitedInputStream;
    }
