    public void test_digest$B() {
        for (int i = 0; i < digestAlgs.length; i++) {
            try {
                MessageDigest digest = MessageDigest.getInstance(digestAlgs[i], providerName);
                assertNotNull(digest);
                digest.digest(AR1);
            } catch (NoSuchAlgorithmException e) {
                fail("getInstance did not find algorithm " + digestAlgs[i]);
            } catch (NoSuchProviderException e) {
                fail("getInstance did not find provider " + providerName);
            }
        }
    }
