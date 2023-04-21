    public void test_digest$BII() {
        for (int i = 0; i < digestAlgs.length; i++) {
            try {
                MessageDigest digest = MessageDigest.getInstance(digestAlgs[i], providerName);
                assertNotNull(digest);
                int len = digest.getDigestLength();
                byte[] digestBytes = new byte[len];
                digest.digest(digestBytes, 0, digestBytes.length);
            } catch (NoSuchAlgorithmException e) {
                fail("getInstance did not find algorithm " + digestAlgs[i]);
            } catch (NoSuchProviderException e) {
                fail("getInstance did not find provider " + providerName);
            } catch (DigestException e) {
                fail("digest caused exception for algorithm " + digestAlgs[i] + " : " + e);
            }
        }
        try {
            MessageDigest.getInstance("SHA").digest(new byte[] {}, Integer.MAX_VALUE, 755);
        } catch (NoSuchAlgorithmException e) {
        } catch (DigestException e) {
        } catch (IllegalArgumentException e) {
        }
    }
