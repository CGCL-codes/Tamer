    public static String getHashValueOfElement(Node nodeToHash) {
        byte[] digestValue = null;
        try {
            digestValue = MessageDigest.getInstance("SHA-1").digest(toCanonical(nodeToHash));
        } catch (Exception ex) {
            LogHolder.log(LogLevel.WARNING, LogType.PAY, "could not create hash value of node");
            return null;
        }
        return Base64.encode(digestValue, false);
    }
