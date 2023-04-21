    public static void write(InputStream in, OutputStream out, int l) throws IOException {
        byte[] rec = new byte[0];
        {
            int rtry = 0;
            int MaxCHUNK = 20480;
            int totRead = 0;
            while (l > totRead) {
                int chunkSize = in.available();
                if (chunkSize > MaxCHUNK) chunkSize = MaxCHUNK;
                int readLen = -1;
                rec = new byte[chunkSize];
                readLen = in.read(rec, 0, chunkSize);
                if (readLen == -1) {
                    rtry++;
                    if (rtry > MAX_RETRY) break;
                    try {
                        Thread.sleep(1000);
                    } catch (Throwable e) {
                        tools.util.LogMgr.err(" write Int " + e.toString());
                    }
                    continue;
                } else {
                    totRead = totRead + readLen;
                    out.write(rec, 0, readLen);
                    rtry = 0;
                }
            }
        }
    }
