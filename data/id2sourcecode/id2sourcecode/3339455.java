    private static String GetHashString(String data, CredentialType credentialType) throws NoSuchAlgorithmException {
        byte[] defaultBytes = data.getBytes();
        MessageDigest algorithm = MessageDigest.getInstance(credentialType.toString());
        algorithm.reset();
        algorithm.update(defaultBytes);
        byte messageDigest[] = algorithm.digest();
        data = GetStringFromArray(messageDigest);
        return data;
    }
