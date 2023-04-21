    protected byte[] getNodeAdress() {
        byte[] address = new byte[6];
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteOut);
        try {
            out.writeLong(random.nextLong());
            out.writeInt(hashCode());
            if (ip != null) out.write(ip);
            out.close();
        } catch (IOException e) {
        }
        byte[] randomBytes = byteOut.toByteArray();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(randomBytes);
            byte[] hash = md5.digest();
            System.arraycopy(hash, 0, address, 0, 6);
        } catch (NoSuchAlgorithmException e) {
        }
        address[0] = (byte) (address[0] | (byte) 0x80);
        return address;
    }
