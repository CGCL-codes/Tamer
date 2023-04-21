    private HashMap newVerifiers(byte[] salt, String username, String password, String index) throws IOException {
        cat.debug("==> newVerifiers(salt, \"" + String.valueOf(username) + "\", \"" + String.valueOf(password) + "\", " + String.valueOf(index) + ")");
        String[] mpi = (String[]) configurations.get(index);
        BigInteger N = new BigInteger(1, SaslUtil.fromb64(mpi[0]));
        BigInteger g = new BigInteger(1, SaslUtil.fromb64(mpi[1]));
        HashMap result = new HashMap(6);
        MessageDigest hash;
        BigInteger x, v;
        String verifier, digestID;
        SRP srp;
        for (int i = 0; i < srps.size(); i++) {
            digestID = String.valueOf(i);
            srp = (SRP) srps.get(digestID);
            hash = srp.newDigest();
            hash.update(salt);
            hash.update(srp.userHash(username, password));
            x = new BigInteger(1, hash.digest());
            v = g.modPow(x, N);
            verifier = SaslUtil.tob64(v.toByteArray());
            result.put(digestID, verifier);
        }
        cat.debug("<== newVerifiers()");
        return result;
    }
