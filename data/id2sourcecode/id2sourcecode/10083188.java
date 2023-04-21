    private static int binarysearch(byte[] rawlist, int start, int end) {
        int floor = 0;
        int ceiling = (rawindex.length) / 5;
        while (floor < ceiling - 1) {
            int middle = (floor + ceiling) / 2;
            if (debug) System.out.println("floor:" + floor + " ceiling:" + ceiling + " => " + middle);
            int off = rawindex[middle * 5];
            int len = rawindex[middle * 5 + 4] & 0x1F;
            int d = compare(rawlist, start, end - start, rawdata, off, len);
            if (d < 0) ceiling = middle; else if (d > 0) floor = middle; else return middle * 12;
        }
        int tmp = floor * 5;
        int off = rawindex[tmp++];
        long lengths = ((long) rawindex[tmp++] << 48) | ((long) rawindex[tmp++] << 32) | ((long) rawindex[tmp++] << 16) | ((long) rawindex[tmp++]);
        floor *= 12;
        for (int i = 0; i < 12; i++) {
            int len = (int) (lengths >> (i * 5)) & 0x1F;
            if (compare(rawlist, start, end, rawdata, off, len) == 0) return floor;
            off += len;
            floor++;
        }
        return -1;
    }
