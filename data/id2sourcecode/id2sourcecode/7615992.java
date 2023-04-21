    public long lastModified() {
        if (isURL) {
            try {
                return url.openConnection().getLastModified();
            } catch (IOException e) {
                return 0;
            }
        }
        return file.lastModified();
    }
