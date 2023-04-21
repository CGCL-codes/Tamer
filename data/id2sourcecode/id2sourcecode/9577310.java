    private static void checkCache() {
        if (cacheSize >= cacheMax) {
            int resultSize = save((cacheSize - cacheMin));
            if (resultSize < cacheMin) {
                cacheMin = (resultSize + cacheMin) / 2;
            }
        }
    }
