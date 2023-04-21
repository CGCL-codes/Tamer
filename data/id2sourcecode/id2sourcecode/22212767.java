    private void checkDigest() throws WrongMessageDigestException {
        if (!checked) {
            byte[] result = messageDigest.digest();
            checked = true;
            if (!MessageDigest.isEqual(result, expectedDigest)) {
                failed = "Incorrect message digest for " + url + ": " + HttpmdUtil.digestString(result);
                checkFailed();
            }
        }
    }
