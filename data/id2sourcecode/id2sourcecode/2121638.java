    public static byte[] convertFileEncoding(File inFile, Charset inCharset, @Nullable File outFile, Charset outCharset) throws IOException {
        RandomAccessFile inRandom = new RandomAccessFile(inFile, "r");
        FileChannel inChannel = inRandom.getChannel();
        MappedByteBuffer byteMapper = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, (int) inFile.length());
        CharsetDecoder inDecoder = inCharset.newDecoder();
        CharsetEncoder outEncoder = outCharset.newEncoder();
        CharBuffer cb = inDecoder.decode(byteMapper);
        ByteBuffer outBuffer = null;
        try {
            outBuffer = outEncoder.encode(cb);
            RandomAccessFile outRandom = null;
            FileChannel outChannel = null;
            if (outFile != null) {
                try {
                    outRandom = new RandomAccessFile(outFile, "rw");
                    outChannel = outRandom.getChannel();
                    outChannel.write(outBuffer);
                } finally {
                    if (outChannel != null) {
                        outChannel.close();
                    }
                    if (outRandom != null) {
                        outRandom.close();
                    }
                }
            }
        } finally {
            inChannel.close();
            inRandom.close();
        }
        return outBuffer.array();
    }
