    public byte[] encryptKeyblob(byte[] keyblob, String passphrase) {
        try {
            ByteArrayWriter baw = new ByteArrayWriter();
            String type = "none";
            if (passphrase != null) {
                if (!passphrase.trim().equals("")) {
                    type = "3DES-CBC";
                    byte[] keydata = makePassphraseKey(passphrase);
                    byte[] iv = new byte[8];
                    ConfigurationLoader.getRND().nextBytes(iv);
                    Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
                    KeySpec keyspec = new DESedeKeySpec(keydata);
                    Key key = SecretKeyFactory.getInstance("DESede").generateSecret(keyspec);
                    cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv, 0, cipher.getBlockSize()));
                    ByteArrayWriter data = new ByteArrayWriter();
                    baw.writeString(type);
                    baw.write(iv);
                    data.writeInt(cookie);
                    data.writeBinaryString(keyblob);
                    baw.writeBinaryString(cipher.doFinal(data.toByteArray()));
                    return formatKey(baw.toByteArray());
                }
            }
            baw.writeString(type);
            baw.writeBinaryString(keyblob);
            return formatKey(baw.toByteArray());
        } catch (Exception ioe) {
            return null;
        }
    }
