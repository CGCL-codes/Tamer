    private IntBuffer readCurrentPrimeBuffered(FileInputStream primeBuf, int count) {
        ByteBuffer bytes = ByteBuffer.allocate(SIZE_OF_INT * count);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        try {
            primeBuf.getChannel().read(bytes);
        } catch (IOException e) {
            return null;
        }
        bytes.rewind();
        return bytes.asIntBuffer();
    }
