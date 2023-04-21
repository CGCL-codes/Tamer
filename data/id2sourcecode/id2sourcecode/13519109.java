    @Override
    public Long getLastModifiedTime() {
        try {
            return url.openConnection().getLastModified();
        } catch (IOException e) {
            return null;
        }
    }
