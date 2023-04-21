    public static Cipher getBlowfishCipher(boolean b64, String fn, boolean tf) throws Exception {
        byte[] raw = null;
        File f = new File(fn);
        if (f.exists() && f.length() > 0) raw = FileUtil.getBytesFromFile(f);
        if (raw == null || raw.length < 1) {
            KeyGenerator kgen = KeyGenerator.getInstance("Blowfish");
            String size = System.getProperty("Blowfish.Key.Size");
            if (size != null) kgen.init(Integer.parseInt(size));
            SecretKey skey = kgen.generateKey();
            raw = skey.getEncoded();
            byte[] rawb = raw;
            if (b64) rawb = Base642.encode(raw);
            FileUtil.writeBytesToFile(f, rawb);
        } else if (b64) raw = Base642.decode(raw);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        if (tf) cipher.init(Cipher.ENCRYPT_MODE, skeySpec); else cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        return cipher;
    }
