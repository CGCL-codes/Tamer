    private void copy(InputStream input, OutputStream output, boolean close) throws IOException {
        int numAvailable = 0;
        int readSize = 0;
        int zeroCnt = 0;
        byte[] buf = new byte[READSIZE];
        while (true) {
            if (numAvailable <= 0) {
                numAvailable = input.available();
            }
            if (numAvailable == 0) {
                if (dieNow == true && zeroCnt++ > 5) {
                    break;
                }
                try {
                    synchronized (this) {
                        wait(WAITTIME);
                    }
                } catch (Exception ignore) {
                }
            } else {
                readSize = (numAvailable > READSIZE) ? READSIZE : numAvailable;
                numAvailable -= readSize;
                int read = input.read(buf, 0, readSize);
                output.write(buf, 0, read);
                output.flush();
            }
        }
        if (close) {
            input.close();
            output.close();
        } else {
            output.flush();
        }
    }
