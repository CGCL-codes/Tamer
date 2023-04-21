    public byte[] encrypt(String sharedKey) throws Exception {
        byte[] key = sharedKey != null ? sharedKey.getBytes("UTF-8") : new byte[] {};
        byte[] data = this.messageText.getBytes();
        byte[] digest = Security.digest(this.messageText.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(digest);
        out.write(data);
        byte[] encryptedData = Security.encrypt(key, out.toByteArray());
        return encryptedData;
    }
