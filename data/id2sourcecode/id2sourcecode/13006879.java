    public byte[] receivePacket() throws IOException, SocketClosedException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int read = inputstream.read();
        while (read != 0) {
            if (read == -1) {
                throw new SocketClosedException();
            }
            buffer.write(read);
            read = inputstream.read();
        }
        return buffer.toByteArray();
    }
