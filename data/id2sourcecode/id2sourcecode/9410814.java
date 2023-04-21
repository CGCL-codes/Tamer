        public FileChannel getFileChannel(File file, String openMode) throws IOException {
            return new FileInputStream(file).getChannel();
        }
