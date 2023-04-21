    protected Request peekKey() throws IOException {
        int n;
        do {
            if ((n = readn(in, sbuf, 0, 4)) < 4) return null;
        } while (sbuf[0] == (byte) 0x85);
        if ((n = readn(in, sbuf, 4, 32)) < 32) return null;
        if (log.level > 2) {
            log.println("New data read: " + this);
            jcifs.util.Hexdump.hexdump(log, sbuf, 4, 32);
        }
        for (; ; ) {
            if (sbuf[0] == (byte) 0x00 && sbuf[1] == (byte) 0x00 && sbuf[4] == (byte) 0xFF && sbuf[5] == (byte) 'S' && sbuf[6] == (byte) 'M' && sbuf[7] == (byte) 'B') {
                break;
            }
            for (int i = 0; i < 35; i++) {
                sbuf[i] = sbuf[i + 1];
            }
            int b;
            if ((b = in.read()) == -1) return null;
            sbuf[35] = (byte) b;
        }
        key.mid = Encdec.dec_uint16le(sbuf, 34);
        return key;
    }
