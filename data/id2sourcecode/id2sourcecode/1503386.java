    public static long copy(InputStream is, OutputStream os) throws IOException {
        byte buffer[] = new byte[1024];
        int readed;
        long length = 0;
        while ((readed = is.read(buffer)) != -1) {
            if (readed > 0) {
                length += readed;
                os.write(buffer, 0, readed);
            } else {
                TimerHelper.notSafeSleep(100);
            }
        }
        return length;
    }
