    public static void write(InputStream in, OutputStream out, long mL, LongNum ln) throws IOException {
        byte[] rec = new byte[0];
        int maxretry = MAX_RETRY * 100;
        int rtry = 0;
        int MaxCHUNK = 228960;
        long rL = 0;
        int ct = 0;
        int pc = 0;
        while (mL > rL) {
            int chunkSize = in.available();
            if (chunkSize > MaxCHUNK) chunkSize = MaxCHUNK;
            int readLen = -1;
            if (chunkSize < 1) chunkSize = 1024;
            rec = new byte[chunkSize];
            readLen = in.read(rec, 0, chunkSize);
            if (readLen < 1) {
                rtry++;
                try {
                    Thread.sleep(10);
                    pc++;
                } catch (Throwable e) {
                    tools.util.LogMgr.err(" write Long " + e.toString());
                }
                if (rtry > maxretry) {
                    tools.util.LogMgr.debug("Ending Read ");
                    break;
                }
                continue;
            } else {
                rL = rL + readLen;
                if (ln != null) ln.increment(readLen);
                out.write(rec, 0, readLen);
                rtry = 0;
            }
        }
        if (pc > 600) tools.util.LogMgr.debug("READ PAUSED IN MINUTES " + (pc / 1200));
    }
