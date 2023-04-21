    public void digest(String[] algs, Map digests, InputStream in) throws DigestException, IOException {
        MessageDigest md;
        byte[] buffer;
        List list;
        int i;
        int n;
        if (in == null || digests == null || algs == null) {
            throw new NullPointerException("Algs, Map, or input stream");
        }
        list = resolveAlgorithmNames(algs);
        if (threshold_ > list.size()) {
            throw new DigestException("Not enough digests of trusted algorithms!");
        }
        buffer = new byte[BUFFER_SIZE];
        while ((n = in.read(buffer)) > 0) {
            for (i = 0; i < list.size(); i += 2) {
                ((MessageDigest) list.get(i)).update(buffer, 0, n);
            }
        }
        digests.clear();
        for (i = 0; i < list.size(); i += 2) {
            md = (MessageDigest) list.get(i);
            digests.put(list.get(i + 1), md.digest());
        }
    }
