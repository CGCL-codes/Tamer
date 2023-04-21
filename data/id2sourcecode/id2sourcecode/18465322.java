    private String getFingerPrint(MessageDigest md, int size) throws Exception {
        byte[] kblob = getPublicKeyBlob();
        if (kblob == null) return null;
        md.reset();
        md.update(kblob, 0, kblob.length);
        byte[] foo = md.digest();
        StringBuffer sb = new StringBuffer();
        int bar;
        for (int i = 0; i < foo.length; i++) {
            bar = foo[i] & 0xff;
            sb.append(chars[(bar >>> 4) & 0xf]);
            sb.append(chars[(bar) & 0xf]);
            if (i + 1 < foo.length) sb.append(":");
        }
        return "ssh-rsa " + size + " " + sb.toString();
    }
