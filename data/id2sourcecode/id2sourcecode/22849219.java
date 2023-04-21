    private long urlLastModified(String url) {
        try {
            return createURL(url).openConnection().getLastModified();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
