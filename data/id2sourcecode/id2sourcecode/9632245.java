    public final void writeBytesToFileNIO(byte[] src, File file) throws IOException {
        FileOutputStream stream = null;
        FileChannel fFileChannel = null;
        try {
            stream = new FileOutputStream(file);
            fFileChannel = stream.getChannel();
            ByteBuffer buf = getByteBuffer(nio_segment_size);
            int index = 0;
            while (index < src.length) {
                buf.rewind();
                int remaining = src.length - index;
                int size = Math.min(nio_segment_size, remaining);
                if (size != buf.capacity()) {
                    buf = ByteBuffer.allocate(size);
                }
                buf.put(src, index, size);
                buf.flip();
                int written = fFileChannel.write(buf);
                buf.flip();
                index += written;
            }
        } finally {
            try {
                if (fFileChannel != null) fFileChannel.close();
                if (stream != null) stream.close();
            } catch (Exception e) {
                Debug.debug(e);
            }
        }
    }
