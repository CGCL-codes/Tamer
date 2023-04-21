    public static synchronized void writeLog(Logging.Level lvl, String description, byte[] data) {
        StringBuffer strdata = new StringBuffer();
        StringBuffer pos = new StringBuffer();
        StringBuffer hex = new StringBuffer();
        StringBuffer str = new StringBuffer();
        for (int b = 0; b < data.length; b += 16) {
            String one;
            pos.delete(0, pos.length());
            hex.delete(0, hex.length());
            str.delete(0, str.length());
            while (pos.length() < 4) pos.insert(0, "0");
            for (int u = 0; u < 16; u++) {
                if (b + u < data.length) {
                    int value = data[b + u];
                    if (value < 0) value += 128;
                    one = new BigInteger("" + value).toString(16);
                    while (one.length() < 2) one = "0" + one;
                    hex.append(one);
                    if (u == 7) {
                        hex.append("-");
                    } else {
                        hex.append(" ");
                    }
                    if (value >= 32 && value <= 127) {
                        str.append(new String(new byte[] { (byte) value }));
                    } else {
                        str.append(".");
                    }
                } else {
                    hex.append("   ");
                    str.append(" ");
                }
            }
            strdata.append(pos + ":  " + hex + "  " + str + "\r\n");
        }
        for (int t = 0; t < logs.size(); t++) {
            ((Logging) logs.get(t)).writeLog(lvl, ThreadId.getCurrentId(), description, strdata.toString());
        }
    }
