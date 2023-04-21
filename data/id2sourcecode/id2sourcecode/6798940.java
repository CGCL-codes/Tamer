    @Override
    public long getLastModified() {
        try {
            return url.openConnection().getLastModified();
        } catch (IOException e) {
            return 0L;
        }
    }
