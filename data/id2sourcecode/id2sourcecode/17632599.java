    private int computeClassId(String className) {
        int hash = 0;
        try {
            hashOut.writeUTF(className);
            hashOut.flush();
            byte hasharray[] = messageDigest.digest();
            int len = hasharray.length;
            if (len > 8) len = 8;
            for (int i = 0; i < len; i++) {
                hash += (hasharray[i] & 255) << (i * 4);
            }
            hash &= 0x7FFFFFFF;
        } catch (IOException ignore) {
            hash = -1;
        }
        return hash;
    }
