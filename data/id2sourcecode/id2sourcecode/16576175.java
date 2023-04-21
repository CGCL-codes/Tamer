    public alto.io.Input openInput() throws java.io.IOException {
        URL url = this.url;
        if (null != url) {
            URLConnection connection = url.openConnection();
            if (connection instanceof alto.net.Connection) {
                ((alto.net.Connection) connection).setReference(this);
            }
            return new ReferenceInputStream(this, connection);
        }
        HttpMessage container = this.read();
        if (null != container) return new ReferenceInputStream(this, container); else return null;
    }
