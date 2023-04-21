    public static void write2(InputStream in, ObjectOutput out) throws IOException {
        byte[] rec = new byte[0];
        int readLen = -1;
        {
            int rtry = 0;
            int MaxCHUNK = 20480;
            while (in.available() > 0) {
                int chunkSize = in.available();
                if (chunkSize > MaxCHUNK) chunkSize = MaxCHUNK;
                readLen = -1;
                rec = new byte[chunkSize];
                readLen = in.read(rec, 0, chunkSize);
                if (readLen == -1) {
                    rtry++;
                    if (rtry > MAX_RETRY) break;
                    continue;
                } else {
                    out.write(rec, 0, readLen);
                    rtry = 0;
                }
            }
        }
    }
