    public boolean canRead() {
        boolean ret = false;
        try {
            this.url.openStream();
            ret = true;
        } catch (Exception ex) {
        }
        return ret;
    }
