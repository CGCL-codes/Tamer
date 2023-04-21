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
        String s = "Server -> Agent: AgentName " + this.username + "\n" + buffer.toString();
        synchronized (logger) {
            logger.log(Level.ALL, s);
        }
        return buffer.toByteArray();
    }
