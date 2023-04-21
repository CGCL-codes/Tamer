    public static void readStreamTo(InputStream in, int len, OutputStream out) {
        DataInputStream din = new DataInputStream(in);
        byte[] rec = new byte[0];
        try {
            if (len > 0) {
                int rtry = 0;
                for (int readCount = 0; readCount < len; ) {
                    int chunkSize = len - readCount;
                    if (chunkSize > MaxCHUNK) chunkSize = MaxCHUNK;
                    if (din.available() < chunkSize) chunkSize = din.available();
                    int readLen = -1;
                    rec = new byte[chunkSize];
                    readLen = din.read(rec, 0, chunkSize);
                    if (readLen == -1) {
                        rtry++;
                        Thread.sleep(1000);
                        if (rtry > MAX_RETRY) break;
                        continue;
                    } else {
                        out.write(rec, 0, readLen);
                        rtry = 0;
                        readCount += readLen;
                    }
                }
            }
        } catch (Throwable e) {
            tools.util.LogMgr.err("READ STREAM TO ERROR " + len);
            e.printStackTrace();
        }
    }
