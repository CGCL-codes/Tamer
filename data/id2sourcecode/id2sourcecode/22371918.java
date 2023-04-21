        private static ByteBuffer asByteBuffer(final RandomAccessFile raf) throws IOException {
            final FileChannel fileChannel = raf.getChannel();
            final long size = fileChannel.size();
            final MappedByteBuffer mmap = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, size);
            return mmap;
        }
