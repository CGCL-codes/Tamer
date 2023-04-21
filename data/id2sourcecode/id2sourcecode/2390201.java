    public long getVersion() {
        try {
            return url.openConnection().getDate();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return -1;
        }
    }
