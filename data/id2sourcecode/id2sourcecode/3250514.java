    @Override
    public long size() {
        if (cachedSize == -2) try {
            cachedSize = url.openConnection().getContentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cachedSize;
    }
