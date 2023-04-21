        public NIOFSIndexInput(File path, int bufferSize) throws IOException {
            super(path, bufferSize);
            channel = file.getChannel();
        }
