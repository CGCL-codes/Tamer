    public BitReader openReader() throws BitStreamException {
        try {
            switch(mode) {
                case MEMORY:
                    return new ByteArrayBitReader(getBytes());
                case STREAM:
                    return new InputStreamBitReader(new BufferedInputStream(new FileInputStream(file), bufferSize));
                case CHANNEL:
                    return new FileChannelBitReader(new RandomAccessFile(file, "r").getChannel(), bufferSize, true);
                default:
                    throw new IllegalStateException("Unexpected mode: " + mode);
            }
        } catch (IOException e) {
            throw new BitStreamException(e);
        }
    }
