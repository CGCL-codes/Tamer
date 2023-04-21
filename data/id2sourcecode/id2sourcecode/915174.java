    protected static final boolean isServiceAvailable(String service) {
        try {
            URL url = new URL(server + service);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String header = connection.getHeaderField(null);
            connection.disconnect();
            String[] fields = header.split(" ");
            if (fields.length > 1) {
                if (fields[1].equals("200")) {
                    return true;
                }
                return false;
            }
        } catch (Exception ex) {
        }
        return false;
    }
