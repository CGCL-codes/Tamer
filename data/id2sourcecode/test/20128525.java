    private long getLastModifiedTime(URL url) {
        try {
            return url.openConnection().getLastModified();
        } catch (IOException e) {
            return 0;
        }
    }
