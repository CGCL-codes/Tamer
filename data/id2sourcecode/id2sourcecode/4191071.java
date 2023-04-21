    private void initIndex(File f) throws IOException {
        file = f;
        if (file.length() == 0) {
            FileOutputStream fos = new FileOutputStream(file);
            FileChannel channel = fos.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(RECORD_SIZE);
            buffer.putInt(socketCount);
            buffer.putInt(socketCount);
            buffer.flip();
            channel.write(buffer, 0);
            for (int i = 0; i < socketCount; i++) {
                buffer.clear();
                buffer.putInt(-1);
                buffer.putInt(-1);
                buffer.flip();
                channel.write(buffer, (long) (byteNumber(i)));
            }
            channel.force(true);
            buffer = null;
            channel.close();
            fos.close();
        }
    }
