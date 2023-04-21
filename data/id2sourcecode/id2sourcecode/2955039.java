    public static ByteBuffer readFileToBuffer(File file, boolean allocateDirect) throws IOException {
        if (file == null) {
            String message = Logging.getMessage("nullValue.FileIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        FileInputStream is = new FileInputStream(file);
        try {
            FileChannel fc = is.getChannel();
            int size = (int) fc.size();
            ByteBuffer buffer = allocateDirect ? ByteBuffer.allocateDirect(size) : ByteBuffer.allocate(size);
            for (int count = 0; count >= 0 && buffer.hasRemaining(); ) {
                count = fc.read(buffer);
            }
            buffer.flip();
            return buffer;
        } finally {
            WWIO.closeStream(is, file.getPath());
        }
    }
