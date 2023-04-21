    public void send(InputStream body, MimeType type) throws IOException {
        setMimeType(type);
        long bodyLength = body.available();
        _exchange.sendResponseHeaders(_status, bodyLength);
        OutputStream bodyStream = _exchange.getResponseBody();
        while (body.available() > 0) bodyStream.write(body.read());
        bodyStream.close();
    }
