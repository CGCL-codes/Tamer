    public static void readStreamTo2(ObjectInput din, OutputStream out, int len, String name) throws IOException {
        byte[] rec = new byte[0];
        int readCount = 0;
        int oc = -4;
        try {
            if (len > 0) {
                oc = din.available();
                int rtry = 0;
                while (readCount < len) {
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
            int av = -3;
            try {
                av = din.available();
            } catch (Throwable e2) {
            }
            if (name == null) e.printStackTrace();
            tools.util.LogMgr.err(av + " READ OBJ STREAM TO ERROR " + len + ":" + name + ":" + readCount + ":" + oc + ":" + new java.util.Date() + ":" + e.toString());
            throw new IOException(av + " READ OBJ STREAM TO ERROR " + len + ":" + name + ":" + readCount + ":" + oc + ":" + new java.util.Date() + ":" + e.toString());
        }
    }
