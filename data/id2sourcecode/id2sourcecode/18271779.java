    public byte[] resetObjectReference(Reference objectReference) {
        int objectNumber = objectReference.getObjectNumber();
        int generationNumber = objectReference.getGenerationNumber();
        int n = encryptionKey.length;
        byte[] step2Bytes = new byte[n + 5];
        System.arraycopy(encryptionKey, 0, step2Bytes, 0, n);
        step2Bytes[n] = (byte) (objectNumber & 0xff);
        step2Bytes[n + 1] = (byte) (objectNumber >> 8 & 0xff);
        step2Bytes[n + 2] = (byte) (objectNumber >> 16 & 0xff);
        step2Bytes[n + 3] = (byte) (generationNumber & 0xff);
        step2Bytes[n + 4] = (byte) (generationNumber >> 8 & 0xff);
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException builtin) {
        }
        md5.update(step2Bytes);
        return md5.digest();
    }
