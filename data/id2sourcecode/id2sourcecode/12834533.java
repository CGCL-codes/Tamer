    private String encode(String src, char[] passwd, MessageDigest md) {
        md.update(src.getBytes());
        if (passwd != null) {
            byte[] passwdBytes = new byte[passwd.length];
            for (int i = 0; i < passwd.length; i++) passwdBytes[i] = (byte) passwd[i];
            md.update(passwdBytes);
            Arrays.fill(passwdBytes, (byte) 0x00);
        }
        byte[] digest = md.digest();
        StringBuffer res = new StringBuffer(digest.length * 2);
        for (int i = 0; i < digest.length; i++) {
            int hashchar = ((digest[i] >>> 4) & 0xf);
            res.append(charArray[hashchar]);
            hashchar = (digest[i] & 0xf);
            res.append(charArray[hashchar]);
        }
        return res.toString();
    }
