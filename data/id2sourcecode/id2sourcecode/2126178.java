    public void test_clone() {
        for (int i = 0; i < digestAlgs.length; i++) {
            try {
                MessageDigest d1 = MessageDigest.getInstance(digestAlgs[i], providerName);
                for (byte b = 0; b < 84; b++) {
                    d1.update(b);
                }
                MessageDigest d2 = (MessageDigest) d1.clone();
                d1.update((byte) 1);
                d2.update((byte) 1);
                assertTrue("cloned hash differs from original for algorithm " + digestAlgs[i], MessageDigest.isEqual(d1.digest(), d2.digest()));
            } catch (CloneNotSupportedException e) {
            } catch (NoSuchAlgorithmException e) {
                fail("getInstance did not find algorithm " + digestAlgs[i]);
            } catch (NoSuchProviderException e) {
                fail("getInstance did not find provider " + providerName);
            }
        }
    }
