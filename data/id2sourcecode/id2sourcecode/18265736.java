    public String PrefixForByteCount(String s, int maxbytecount) throws Exception {
        byte[] inputbytes = BplusTree.StringToBytes(s);
        java.security.MessageDigest D = java.security.MessageDigest.getInstance("MD5");
        byte[] digest = D.digest(inputbytes);
        byte[] resultbytes = new byte[maxbytecount];
        for (int i = 0; i < maxbytecount; i++) {
            int r = digest[i % digest.length];
            if (r < 0) {
                r = -r;
            }
            r = r % 79 + 40;
            resultbytes[i] = (byte) r;
        }
        String result = BplusTree.BytesToString(resultbytes);
        return result;
    }
