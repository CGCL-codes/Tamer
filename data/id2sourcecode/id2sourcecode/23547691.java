    public synchronized void save(OutputStream out, String header, String password, String cipherName) throws IOException {
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        Properties encProps = new Properties();
        byte[] contents, hash;
        String hashStr;
        SSHCipher cipher = SSHCipher.getInstance(cipherName);
        int size;
        if (cipher == null) throw new IOException("Unknown cipher '" + cipherName + "'");
        save(bytesOut, header);
        contents = bytesOut.toByteArray();
        size = contents.length;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(contents);
            hash = md5.digest();
        } catch (Exception e) {
            throw new IOException("MD5 not implemented, can't generate session-id");
        }
        hash = Base64.encode(hash);
        hashStr = new String(hash);
        byte[] tmp = new byte[contents.length + (8 - (contents.length % 8))];
        System.arraycopy(contents, 0, tmp, 0, contents.length);
        contents = new byte[tmp.length];
        cipher.setKey(hashStr + password);
        cipher.encrypt(tmp, 0, contents, 0, contents.length);
        contents = Base64.encode(contents);
        encProps.put(HASH_KEY, new String(hash));
        encProps.put(CIPHER_KEY, cipherName.substring(3));
        encProps.put(CONTENTS_KEY, new String(contents));
        encProps.put(SIZE_KEY, String.valueOf(size));
        encProps.save(out, PROPS_HEADER);
        out.flush();
    }
