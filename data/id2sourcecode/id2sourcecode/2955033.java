    public static MappedByteBuffer mapFile(File file, FileChannel.MapMode mode) throws IOException {
        if (file == null) {
            String message = Logging.getMessage("nullValue.FileIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        if (mode == null) {
            String message = Logging.getMessage("nullValue.ModelIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        String accessMode;
        if (mode == FileChannel.MapMode.READ_ONLY) accessMode = "r"; else accessMode = "rw";
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, accessMode);
            FileChannel fc = raf.getChannel();
            return fc.map(mode, 0, fc.size());
        } finally {
            WWIO.closeStream(raf, file.getPath());
        }
    }
