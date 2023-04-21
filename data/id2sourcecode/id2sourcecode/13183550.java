    public static void write(SeekableRandomFile output, int address, int length) throws IOException {
        Buffer buffer = Memory.getInstance().getBuffer(address, length);
        if (buffer instanceof ByteBuffer) {
            output.getChannel().write((ByteBuffer) buffer);
        } else if (length > 0) {
            byte[] bytes = new byte[length];
            IMemoryReader memoryReader = MemoryReader.getMemoryReader(address, length, 1);
            for (int i = 0; i < length; i++) {
                bytes[i] = (byte) memoryReader.readNext();
            }
            output.write(bytes);
        }
    }
