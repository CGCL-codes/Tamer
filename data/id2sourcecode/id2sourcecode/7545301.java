    public static void pump(InputStream in, OutputStream out, int threshold, Lock lock, PumpListener listener) throws IOException {
        int totalRead = 0;
        boolean fired = (threshold == 0);
        byte[] buffer = new byte[bufflen];
        int bytes_read = 0;
        int times = 0;
        while (bytes_read != -1) {
            times = times + 1;
            bytes_read = in.read(buffer);
            if (bytes_read == -1) break;
            totalRead = totalRead + bytes_read;
            out.write(buffer, 0, bytes_read);
            if (!fired && totalRead > threshold) {
                if (lock != null) lock.unlock();
                fired = true;
            }
            if (fired && bytes_read == bufflen) {
                bufflen = bufflen * 2;
                buffer = new byte[bufflen];
                System.err.println("Increased buffer: " + bufflen);
            }
        }
        if (lock != null) lock.unlock();
        if (listener != null) listener.fireDone();
    }
