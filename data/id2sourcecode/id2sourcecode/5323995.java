    private synchronized AlgorithmParameters getIV(Key key, int seed) throws NoSuchAlgorithmException, InvalidParameterSpecException {
        if (seed == 0) {
            return nullIv;
        } else {
            AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
            byte[] ivData = new byte[KEY_LEN];
            Util.toBytes(seed, ivData, 0, 4);
            Util.toBytes(seed, ivData, 4, 4);
            Util.toBytes(seed, ivData, 8, 4);
            Util.toBytes(seed, ivData, 12, 4);
            sha.update(key.getEncoded());
            sha.update(ivData);
            params.init(new IvParameterSpec(sha.digest(), 2, 16));
            return params;
        }
    }
