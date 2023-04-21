    private static void printMD(byte[] encoding, String mdAlgorithm, String providerName) throws NoSuchAlgorithmException, NoSuchProviderException {
        byte[] digest;
        try {
            if (providerName != null) {
                digest = MessageDigest.getInstance(mdAlgorithm, providerName).digest(encoding);
            } else {
                digest = MessageDigest.getInstance(mdAlgorithm).digest(encoding);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException("The algorithm " + mdAlgorithm + " is not found in the environment.", e);
        } catch (NoSuchProviderException e) {
            throw (NoSuchProviderException) new NoSuchProviderException("The provider " + providerName + " is not found in the environment.").initCause(e);
        }
        System.out.println(formatBytes(digest));
    }
