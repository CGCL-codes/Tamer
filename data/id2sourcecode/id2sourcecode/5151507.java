    @SuppressWarnings("unchecked")
    public void removeBatch(int batchIndex) {
        ArrayList<BoneInfluence>[][] newCache = new ArrayList[skin.getBatchCount()][];
        for (int x = 0; x < cache.length - 1; x++) {
            if (x < batchIndex) newCache[x] = cache[x]; else newCache[x] = cache[x + 1];
        }
        cache = newCache;
    }
