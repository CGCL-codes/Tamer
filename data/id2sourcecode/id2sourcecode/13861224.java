    public long lastModified() {
        if (file != null) {
            return file.lastModified();
        } else if (url != null) {
            try {
                return url.openConnection().getLastModified();
            } catch (IOException e) {
                return Long.MAX_VALUE;
            }
        }
        return 0;
    }
