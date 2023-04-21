    public CBC(String private_key) {
        byte[] pvtkey = new byte[56];
        if (private_key.length() == 111) private_key += "0";
        System.out.println("key length = " + private_key.length());
        for (int i = 0; i < 56; i++) pvtkey[i] = (byte) Short.parseShort(private_key.substring(i * 2, i * 2 + 2), 16);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] realkey = new byte[56];
        byte[] hash = md.digest(pvtkey);
        System.arraycopy(hash, 0, realkey, 0, 16);
        md.update(realkey, 0, 16);
        hash = md.digest();
        System.arraycopy(hash, 0, realkey, 16, 16);
        md.update(realkey, 0, 32);
        hash = md.digest();
        System.arraycopy(hash, 0, realkey, 32, 16);
        md.update(realkey, 0, 48);
        hash = md.digest();
        System.arraycopy(hash, 0, realkey, 48, 8);
        try {
            key = new SecretKeySpec(realkey, "Blowfish");
            cipher = Cipher.getInstance("Blowfish/CBC/NoPadding", Config.PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
