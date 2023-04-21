    public void digest(String[] algs, Map digests, byte[] data) throws DigestException, IOException {
        MessageDigest md;
        List list;
        int i;
        if (data == null || digests == null || algs == null) {
            throw new NullPointerException("Algs, Map, or data");
        }
        list = resolveAlgorithmNames(algs);
        if (threshold_ > list.size()) {
            throw new DigestException("Not enough digests of trusted algorithms!");
        }
        for (i = 0; i < list.size(); i += 2) {
            ((MessageDigest) list.get(i)).update(data);
        }
        digests.clear();
        for (i = 0; i < list.size(); i += 2) {
            md = (MessageDigest) list.get(i);
            digests.put(list.get(i + 1), md.digest());
        }
    }
