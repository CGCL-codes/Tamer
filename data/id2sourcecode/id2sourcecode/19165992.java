    private static byte[] _digest(String data, Algorithm algorithm) throws NoSuchAlgorithmException {
        if (!Digester.digesters.containsKey(algorithm.algorithm())) {
            Digester.digesters.put(algorithm.algorithm(), MessageDigest.getInstance(algorithm.algorithm()));
        }
        MessageDigest d = Digester.digesters.get(algorithm.algorithm());
        byte[] b = d.digest(data.getBytes());
        d.reset();
        return b;
    }
