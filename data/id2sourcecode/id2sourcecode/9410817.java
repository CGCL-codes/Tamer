        public FileChannel getFileChannel(File file, final String openMode) throws IOException {
            if (openMode != null) {
                return new RandomAccessFile(file, openMode).getChannel();
            }
            return new FileOutputStream(file).getChannel();
        }
