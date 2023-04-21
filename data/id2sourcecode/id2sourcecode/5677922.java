    public static int redirectInput(InputStream from, OutputStream to, byte[] readBuffer, int flushEveryNBytes) throws IOException {
        int nRead = 0;
        int totalRead = 0;
        int unflushed = 0;
        while (nRead != -1) {
            if (nRead > 0) {
                to.write(readBuffer, 0, nRead);
                totalRead += nRead;
                unflushed += nRead;
                if (unflushed >= flushEveryNBytes) {
                    unflushed = 0;
                    to.flush();
                }
            }
            nRead = from.read(readBuffer);
        }
        return totalRead;
    }
