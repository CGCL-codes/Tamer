    public String getCheckSum(byte[] bytes) {
        InputStream inputStream = null;
        String output = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            inputStream = new ByteArrayInputStream(bytes);
            byte[] buffer = new byte[BUFFER_SIZE];
            int read = 0;
            while ((read = inputStream.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            output = bigInt.toString(16);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading bytes", e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Error with algorithm", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new IllegalArgumentException("Error reading bytes", e);
            }
        }
        return output;
    }
