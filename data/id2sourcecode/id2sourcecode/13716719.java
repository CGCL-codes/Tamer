    public void delete(RandomAccessFile file) throws IOException {
        byte[] buffer = new byte[FIELD_TAGID_LENGTH];
        final FileChannel fc = file.getChannel();
        fc.position();
        ByteBuffer byteBuffer = ByteBuffer.allocate(TAG_HEADER_LENGTH);
        fc.read(byteBuffer, 0);
        byteBuffer.flip();
        if (seek(byteBuffer)) {
            file.seek(0L);
            file.write(buffer);
        }
    }
