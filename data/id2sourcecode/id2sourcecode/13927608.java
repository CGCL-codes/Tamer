    public static List<byte[]> createParentGeneration(List<byte[]> nodes, MessageDigest md) {
        md.reset();
        int size = nodes.size();
        size = size % 2 == 0 ? size / 2 : (size + 1) / 2;
        List<byte[]> ret = new ArrayList<byte[]>(size);
        Iterator<byte[]> iter = nodes.iterator();
        while (iter.hasNext()) {
            byte[] left = iter.next();
            if (iter.hasNext()) {
                byte[] right = iter.next();
                md.reset();
                md.update(HashTreeUtils.INTERNAL_HASH_PREFIX);
                md.update(left, 0, left.length);
                md.update(right, 0, right.length);
                byte[] result = md.digest();
                ret.add(result);
            } else {
                ret.add(left);
            }
        }
        return ret;
    }
