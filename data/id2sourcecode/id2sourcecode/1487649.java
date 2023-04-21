    protected void engineSetSeed(byte[] seed) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            if (xi == null) {
                int seedBits = 8 * seed.length;
                if (seedBits > 160) seedBits = 160;
                bitsTotal = log2(seedBits);
            } else {
                sha1.update(xi.toByteArray());
            }
            sha1.update(seed);
            xi = new BigInteger(1, sha1.digest());
            while (!(xi.gcd(n)).equals(one)) {
                xi = xi.add(one);
            }
            nextXi();
        } catch (Exception e) {
            throw new Error("Error in BlumBlumShub engine: " + e);
        }
    }
