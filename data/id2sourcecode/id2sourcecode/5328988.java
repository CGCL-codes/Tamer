    public synchronized OutputStream getOutputStream(String contenttype) throws IOException {
        if ("".equals(contenttype)) {
            contenttype = POST_CONTENT_TYPE;
        }
        if (this.is != null && this.os == null) {
            throw new ProtocolException("Cannot write output after reading input");
        }
        if (this.os == null) {
            connect();
            String header = HTTPProtocol.createPUTHeader(url.getFile(), url.getHost() + ":" + port, USER_AGENT, contenttype, -1, true);
            OutputStream wrapped = socket.getOutputStream();
            wrapped.write(header.getBytes());
            this.os = new HTTPChunkedOutputStream(wrapped);
        }
        return os;
    }
