    public void extractMessages(File file, final MessageListener listener) throws IOException, ProtocolCodecException {
        FileChannel readOnlyChannel = new RandomAccessFile(file, "r").getChannel();
        MappedByteBuffer memoryMappedBuffer = readOnlyChannel.map(FileChannel.MapMode.READ_ONLY, 0, (int) readOnlyChannel.size());
        decode(null, ByteBuffer.wrap(memoryMappedBuffer), new ProtocolDecoderOutput() {

            public void write(Object message) {
                listener.onMessage((String) message);
            }

            public void flush() {
            }
        });
    }
