        private MMapIndexInput(RandomAccessFile raf) throws IOException {
            this.length = raf.length();
            this.buffer = raf.getChannel().map(MapMode.READ_ONLY, 0, length);
        }
