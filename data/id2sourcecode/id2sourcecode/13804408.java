    @Override
    public synchronized OutputStream getOutputStream() throws IOException {
        if (os != null) {
            return os;
        } else if (is != null) {
            throw new IOException("Cannot write output after reading input.");
        }
        connect();
        String to = ParseUtil.decode(url.getPath());
        client.from(getFromAddress());
        client.to(to);
        os = client.startMessage();
        return os;
    }
