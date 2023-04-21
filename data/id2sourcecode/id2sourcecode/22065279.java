    public boolean referenceValidation(byte[] data, byte[] digestVal, String digestMethod) {
        byte[] digested = null;
        try {
            digested = this.digest(data, digestMethod);
        } catch (DigestException de) {
            logger.debug("error: " + de);
            return false;
        }
        if (digested.length != digestVal.length) return false;
        for (int i = 0; i < digested.length; i++) {
            if (digested[i] == digestVal[i]) continue; else return false;
        }
        return true;
    }
