    public final byte[] readIncoming() throws IOException {
        boolean done = false;
        boolean negotiate = false;
        baosin.reset();
        int j = -1;
        int i = 0;
        while (!done) {
            i = bin.read();
            if (j == 255 && i == 255) {
                j = -1;
                continue;
            } else {
                baosin.write(i);
                if (j == 255 && i == 239) done = true;
                if (i == 253 && j == 255) {
                    done = true;
                    negotiate = true;
                }
                j = i;
            }
        }
        if (negotiate) {
            baosin.write(bin.read());
            negotiate(baosin.toByteArray());
        }
        if (dumpBytes) {
            System.out.println("dumping");
            dump(baosin.toByteArray());
        }
        return baosin.toByteArray();
    }
