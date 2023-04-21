    @Override
    public boolean isAvailable() {
        if (this.url == null) {
            return false;
        }
        try {
            long length = this.url.openConnection().getContentLength();
            return (length != -1);
        } catch (IOException e) {
            return false;
        }
    }
