    public long get(File destinationFile, long start, long length) throws IOException {
        if (start < 0L || length < 0L || start + length > length()) return 0L;
        if (actions != null) actions.endAction();
        commitChanges();
        RandomAccessFile dst = new RandomAccessFile(destinationFile, "rws");
        IOException preCloseException = null;
        try {
            dst.setLength(length);
            FileChannel channel = dst.getChannel();
            ByteBuffer buffer = null;
            for (long position = 0L; position < length; position += mappedFileBufferLength) {
                int partLength = (int) Math.min(mappedFileBufferLength, length - position);
                boolean bufferFromMap = true;
                try {
                    buffer = channel.map(FileChannel.MapMode.READ_WRITE, position, partLength);
                } catch (IOException e) {
                    bufferFromMap = false;
                    if (buffer == null) buffer = ByteBuffer.allocateDirect((int) mappedFileBufferLength);
                    buffer.position(0);
                    buffer.limit(partLength);
                }
                get(buffer, start + position);
                if (bufferFromMap) {
                    ((MappedByteBuffer) buffer).force();
                    buffer = null;
                } else {
                    buffer.position(0);
                    buffer.limit(partLength);
                    channel.write(buffer, position);
                }
            }
            channel.force(true);
            channel.close();
        } catch (IOException e) {
            preCloseException = e;
        }
        try {
            dst.close();
        } catch (IOException e) {
            if (preCloseException == null) throw e;
            throw preCloseException;
        }
        if (preCloseException != null) throw preCloseException;
        return length;
    }
