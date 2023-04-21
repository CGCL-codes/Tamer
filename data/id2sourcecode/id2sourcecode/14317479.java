    public java.security.cert.Certificate[] verify(Hashtable verifiedCerts, Hashtable sigFileCerts) throws JarException {
        if (skip) return null;
        if (certs != null) return certs;
        for (int i = 0; i < digests.size(); i++) {
            MessageDigest digest = (MessageDigest) digests.get(i);
            byte[] manHash = (byte[]) manifestHashes.get(i);
            byte[] theHash = digest.digest();
            if (debug != null) {
                debug.println("Manifest Entry: " + name + " digest=" + digest.getAlgorithm());
                debug.println("  manifest " + toHex(manHash));
                debug.println("  computed " + toHex(theHash));
                debug.println();
            }
            if (!MessageDigest.isEqual(theHash, manHash)) throw new SecurityException(digest.getAlgorithm() + " digest error for " + name);
        }
        certs = (java.security.cert.Certificate[]) sigFileCerts.remove(name);
        if (certs != null) {
            verifiedCerts.put(name, certs);
        }
        return certs;
    }
