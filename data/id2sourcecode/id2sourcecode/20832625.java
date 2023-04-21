    public boolean exists() {
        try {
            url.openConnection().connect();
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
