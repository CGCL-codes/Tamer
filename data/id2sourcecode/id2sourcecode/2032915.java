    protected int emitReplyStream(InputStream in) throws IOException {
        int totalRead = 0;
        if (in != null) {
            byte[] data = new byte[1024];
            for (int read = in.read(data); read != -1; read = in.read(data)) {
                totalRead += read;
                this.socket.write(data, 0, read);
            }
        }
        return totalRead;
    }
