    @Override
    public Long getLength() {
        try {
            URLConnection connection = url.openConnection();
            long length = connection.getContentLength();
            return length == -1 ? null : length;
        } catch (IOException e) {
            return null;
        }
    }
