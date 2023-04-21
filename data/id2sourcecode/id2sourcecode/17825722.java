    @Override
    public boolean isOutOfSync() {
        try {
            URL url = new URL(location);
            return lastModify != url.openConnection().getLastModified();
        } catch (Exception e) {
            logger.warn(e);
            return false;
        }
    }
