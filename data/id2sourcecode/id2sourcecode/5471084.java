    public static boolean inputStream2FileOutputStream(InputStream is, FileOutputStream fos) throws IOException {
        FileChannel channel = null;
        FileLock lock = null;
        ByteBuffer bb = null;
        try {
            channel = fos.getChannel();
            if (channel != null) {
                lock = channel.tryLock();
            }
            bb = getTempByteBuffer();
        } catch (Throwable t) {
        }
        if (lock == null || bb == null || !bb.hasArray()) {
            releaseTempByteBuffer(bb);
            return false;
        }
        try {
            int bytesRead = is.read(bb.array());
            while (bytesRead > 0 || is.available() > 0) {
                if (bytesRead > 0) {
                    int written = 0;
                    if (bytesRead < BUFFER_LEN) {
                        ByteBuffer temp = ByteBuffer.allocate(bytesRead);
                        temp.put(bb.array(), 0, bytesRead);
                        temp.position(0);
                        written = channel.write(temp);
                    } else {
                        bb.position(0);
                        written = channel.write(bb);
                        bb.clear();
                    }
                }
                bytesRead = is.read(bb.array());
            }
        } finally {
            lock.release();
            releaseTempByteBuffer(bb);
        }
        return true;
    }
