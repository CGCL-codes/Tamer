    protected void setHash(InputStream in) throws IOException {
        MessageDigest dig = null;
        try {
            dig = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException caught) {
            dig = null;
        }
        if (dig == null) {
            throw new ProviderException("SHA-1 digest algorithm not found");
        }
        dig.reset();
        byte[] chunk = new byte[1024];
        try {
            do {
                int read = in.read(chunk);
                if (read == -1) {
                    break;
                }
                dig.update(chunk, 0, read);
            } while (true);
        } finally {
            in.close();
        }
        byte[] result = dig.digest();
        System.arraycopy(result, 0, id.bytes, CodatID.codatHashOffset, CodatID.hashSize);
    }
