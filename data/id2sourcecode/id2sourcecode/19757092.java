    byte[] msgDigest(byte[] msg) {
        int blockSize = 4096;
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM, getProviderByAlgorithm(MESSAGE_DIGEST_ALGORITHM));
            ByteArrayInputStream bis = new ByteArrayInputStream(msg);
            byte[] inputBlock = new byte[blockSize];
            int bytesRead = 0;
            while ((bytesRead = bis.read(inputBlock)) != -1) {
                digest.update(inputBlock, 0, bytesRead);
            }
        } catch (GeneralSecurityException e) {
            super.fireOffWarningInformation("MqJmsJceBaseBean:msgDigest - " + e.getMessage());
            return null;
        } catch (java.io.IOException e) {
            super.fireOffWarningInformation("MqJmsJceBaseBean:msgDigest - " + e.getMessage());
            return null;
        }
        return digest.digest();
    }
