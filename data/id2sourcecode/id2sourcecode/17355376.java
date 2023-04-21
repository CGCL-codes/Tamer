    public final byte[] generateHash(String clearTextID, String function) {
        String id;
        if (function == null) {
            id = clearTextID;
        } else {
            id = clearTextID + functionSeperator + function;
        }
        byte[] buffer = id.getBytes();
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance(algorithmType);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Cannot load selected Digest Hash implementation", e);
            return null;
        }
        algorithm.reset();
        algorithm.update(buffer);
        try {
            byte[] digest1 = algorithm.digest();
            return digest1;
        } catch (Exception de) {
            LOG.log(Level.SEVERE, "Failed to creat a digest.", de);
            return null;
        }
    }
