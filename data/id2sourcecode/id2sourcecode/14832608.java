    public static void writeStreamToStream(InputStream input, OutputStream output, Logger log) throws IOException {
        WritableByteChannel out = Channels.newChannel(output);
        ReadableByteChannel in = Channels.newChannel(input);
        Pipe pipe = Pipe.open();
        ChannelWriter writer = new ChannelWriter(in, pipe.sink(), log);
        writer.start();
        ReadableByteChannel pipeSource = pipe.source();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true) {
            buffer.clear();
            try {
                int bytesRead = pipeSource.read(buffer);
                if (bytesRead == -1) break;
            } catch (IOException e) {
                log.error("writeStreamToStream: Error reading from byte" + " channel.", e);
            }
            buffer.flip();
            try {
                while (out.write(buffer) > 0) {
                }
            } catch (IOException e) {
                log.error("writeStreamToStream: Error writing to byte" + " channel.", e);
            }
        }
        in.close();
        out.close();
    }
