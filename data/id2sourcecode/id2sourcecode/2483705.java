    public byte[] decode(int b[]) {
        int intCount = b.length;
        byte outb[] = new byte[intCount * 4];
        int tmp[] = new int[2];
        int i, j;
        for (j = 0, i = 0; i < intCount; i += 2, j += 8) {
            tmp[0] = b[i];
            tmp[1] = b[i + 1];
            decipher(tmp);
            outb[j] = (byte) (tmp[0] >>> 24);
            outb[j + 1] = (byte) (tmp[0] >>> 16);
            outb[j + 2] = (byte) (tmp[0] >>> 8);
            outb[j + 3] = (byte) (tmp[0]);
            outb[j + 4] = (byte) (tmp[1] >>> 24);
            outb[j + 5] = (byte) (tmp[1] >>> 16);
            outb[j + 6] = (byte) (tmp[1] >>> 8);
            outb[j + 7] = (byte) (tmp[1]);
        }
        return outb;
    }
