    public synchronized void load(InputStream in, String password) throws IOException, SSHAccessDeniedException {
        Properties encProps = new Properties();
        String hashStr, cipherName, contentsStr, sizeStr;
        byte[] contents, hash, hashCalc;
        SSHCipher cipher;
        int size;
        encProps.load(in);
        hashStr = encProps.getProperty(HASH_KEY);
        cipherName = "SSH" + encProps.getProperty(CIPHER_KEY);
        contentsStr = encProps.getProperty(CONTENTS_KEY);
        sizeStr = encProps.getProperty(SIZE_KEY);
        if (hashStr == null || cipherName == null || contentsStr == null || sizeStr == null) {
            isNormalPropsFile = true;
            Enumeration keys = encProps.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                put(key, encProps.getProperty(key));
            }
            return;
        }
        size = Integer.parseInt(sizeStr);
        hash = Base64.decode(hashStr.getBytes());
        contents = Base64.decode(contentsStr.getBytes());
        cipher = SSHCipher.getInstance(cipherName);
        if (cipher == null) throw new IOException("Unknown cipher '" + cipherName + "'");
        cipher.setKey(hashStr + password);
        cipher.decrypt(contents, 0, contents, 0, contents.length);
        byte[] tmp = new byte[size];
        System.arraycopy(contents, 0, tmp, 0, size);
        contents = tmp;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(contents);
            hashCalc = md5.digest();
        } catch (Exception e) {
            throw new IOException("MD5 not implemented, can't generate session-id");
        }
        for (int i = 0; i < hash.length; i++) {
            if (hash[i] != hashCalc[i]) throw new SSHAccessDeniedException("Access denied");
        }
        ByteArrayInputStream bytesIn = new ByteArrayInputStream(contents);
        load(bytesIn);
    }
