    public long length() {
        if (isURL) {
            try {
                return url.openConnection().getContentLength();
            } catch (IOException e) {
                return 0;
            }
        }
        return file.length();
    }
