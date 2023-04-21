    public boolean isReachable() {
        try {
            url.openConnection().getInputStream().close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
