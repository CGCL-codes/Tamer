    public final byte[] readIncoming() throws IOException {
        boolean done = false;
        boolean negotiate = false;
        baosin.reset();
        int j = -1;
        int i = 0;
        while (!done) {
            i = bin.read();
            if (i == -1) {
                done = true;
                vt.disconnect();
                continue;
            }
            if (j == 255 && i == 255) {
                j = -1;
                continue;
            }
            baosin.write(i);
            if (j == 255 && i == 239) done = true;
            if (i == 253 && j == 255) {
                done = true;
                negotiate = true;
            }
            j = i;
        }
        byte[] rBytes = baosin.toByteArray();
        if (dumpBytes) {
            dump(rBytes);
        }
        if (negotiate) {
            baosin.write(bin.read());
            vt.negotiate(rBytes);
            return null;
        }
        return rBytes;
    }
