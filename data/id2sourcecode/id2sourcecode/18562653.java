    public static byte[] readStream(InputStream din, int len, int mt, boolean wt) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] rec = null;
        int chunkSize = 0;
        int readCount = 0;
        int cter = 0;
        int readLen = -1;
        int loc = 0;
        try {
            if (len > 0) {
                int rtry = 0;
                for (readCount = 0; readCount < len; ) {
                    if (!wt && din.available() < 1) break;
                    chunkSize = len - readCount;
                    if (chunkSize > MaxCHUNK) chunkSize = MaxCHUNK;
                    if (din.available() < chunkSize) chunkSize = din.available();
                    loc = 0;
                    rec = new byte[chunkSize];
                    loc = 1;
                    readLen = din.read(rec, 0, chunkSize);
                    loc = 2;
                    if (readLen == -1) {
                        rtry++;
                        loc = 3;
                        if (rtry > mt) break;
                        continue;
                    } else {
                        loc = 4;
                        bout.write(rec, 0, readLen);
                        loc = 5;
                        cter++;
                        rtry = 0;
                        readCount += readLen;
                    }
                    loc = 6;
                }
            }
            return bout.toByteArray();
        } catch (Throwable e) {
            tools.util.LogMgr.err(readCount + " READ STREAM ERROR 1 " + len + ":" + chunkSize + ":" + rec.length + ":" + cter + ":" + loc + ":" + readLen);
            e.printStackTrace();
        }
        return bout.toByteArray();
    }
