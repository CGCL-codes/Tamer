    private void copy() throws IOException {
        int numAvailable = 0;
        int readSize = 0;
        int zeroCnt = 0;
        byte[] buf = new byte[READSIZE];
        while (!dieNow || zeroCnt <= 5) {
            if (numAvailable <= 0) {
                numAvailable = input.available();
            }
            if (numAvailable == 0) {
                ++zeroCnt;
                waitSome();
            } else {
                readSize = (numAvailable > READSIZE) ? READSIZE : numAvailable;
                numAvailable -= readSize;
                readSize = input.read(buf, 0, readSize);
                write(buf, readSize);
                output.flush();
                blackBox.put(buf, 0, readSize);
            }
        }
        closeStreams();
    }
