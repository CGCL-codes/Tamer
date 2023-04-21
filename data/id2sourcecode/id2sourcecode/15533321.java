    @Override
    public byte[] generateChecksum(byte[] message) {
        try {
            byte[] input = Arrays.copyOf(message, message.length + password.length);
            System.arraycopy(password, 0, input, message.length, password.length);
            MessageDigest md5 = MessageDigest.getInstance("SHA-256");
            return md5.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
