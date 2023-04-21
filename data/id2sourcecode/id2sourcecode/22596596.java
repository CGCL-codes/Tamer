    public int getSize() {
        if (size == -1) {
            try {
                URL url = new URL(location);
                URLConnection connection = url.openConnection();
                size = connection.getContentLength();
            } catch (Exception ex) {
                size = 0;
            }
        }
        return size;
    }
