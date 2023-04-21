    public long getDate() {
        if (date < 0) {
            try {
                date = url.openConnection().getLastModified();
            } catch (Exception exception) {
                date = -1;
            }
        }
        return date;
    }
