    private InputStream getItemFromPrefilledCache(TransferContent item) {
        String name = "cache/" + item.name;
        String timestamp = prefilledCacheManager.getProperty(item.name);
        if ((timestamp != null) && (Integer.parseInt(timestamp) == item.timestamp)) {
            URL url = SpriteStore.get().getResourceURL(name);
            if (url != null) {
                try {
                    logger.debug("Content " + item.name + " is in prefilled cache.");
                    return url.openStream();
                } catch (IOException e) {
                    logger.error(e, e);
                }
            }
        }
        return null;
    }
