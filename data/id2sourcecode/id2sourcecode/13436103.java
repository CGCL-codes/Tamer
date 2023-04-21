    public int write(byte[] b, int off, int len) throws IOException {
        int total = 0;
        try {
            while (total < len) {
                int written = channel.write(b, off + total, len - total);
                if (written > 0) {
                    total += written;
                    while (channel.write(null, 0, 0) != 0) Thread.sleep(100);
                } else {
                    Thread.sleep(100);
                }
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return total;
    }
