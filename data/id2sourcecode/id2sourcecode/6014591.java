    public synchronized String hash(String str) {
        byte[] bt = mHasher.digest(str.getBytes());
        int l = bt.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = digits[(0xF0 & bt[i]) >>> 4];
            out[j++] = digits[0x0F & bt[i]];
        }
        return new String(out);
    }
