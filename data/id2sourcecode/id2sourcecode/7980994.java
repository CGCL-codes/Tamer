    public byte[] produceHash(byte[] inputData) throws ChainedException {
        final String myName = thisClass + ".produceHash(byte)";
        if (inputData.length == 0) {
            throw new IllegalArgumentException(myName + " inputData's length must not be zero");
        }
        if (logCat.isDebugEnabled()) {
            logCat.debug("Producing a hash for the data: " + new String(inputData));
        }
        return sha.digest(inputData);
    }
