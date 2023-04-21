    public String GenerateDigest(String str, String str1) {
        byte[] KeyBytes;
        byte[] KeyBytes1;
        byte[] Temp;
        MessageDigest Digest, Digest1;
        int key_len = 0;
        int data_len = 0;
        int i = 0;
        int iTemp;
        byte[] k_ipad, k_opad, tk, tk1;
        String strTemp;
        k_ipad = new byte[64];
        k_opad = new byte[64];
        tk = new byte[64];
        tk1 = new byte[64];
        key_len = str1.length();
        KeyBytes = new byte[key_len];
        KeyBytes = str1.getBytes();
        Temp = new byte[key_len];
        if (key_len > 64) {
            try {
                Digest = MessageDigest.getInstance("MD5");
                Digest.update(KeyBytes);
                tk = Digest.digest();
                key_len = 16;
                for (i = 0; i < 16; i++) {
                    KeyBytes[i] = tk[i];
                }
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
                return "";
            }
        }
        for (i = 0; i < key_len; i++) {
            Temp[i] = KeyBytes[i];
            KeyBytes[i] ^= 0x36;
            k_ipad[i] = KeyBytes[i];
            Temp[i] ^= 0x5c;
            k_opad[i] = Temp[i];
        }
        while (i < 64) {
            k_ipad[i] = 0x36;
            k_opad[i] = 0x5c;
            i++;
        }
        try {
            data_len = str.length() / 2;
            KeyBytes1 = new byte[data_len];
            KeyBytes1 = StringToByteArray(str, data_len);
            Digest = MessageDigest.getInstance("MD5");
            Digest.update(k_ipad);
            Digest.update(KeyBytes1);
            tk = Digest.digest();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return "";
        }
        try {
            Digest1 = MessageDigest.getInstance("MD5");
            Digest1.update(k_opad);
            Digest1.update(tk);
            tk1 = Digest1.digest();
            return (ByteArrayToString(tk1, 16));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return "";
        }
    }
