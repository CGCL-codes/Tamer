    @Override
    public boolean delete() {
        try {
            URLConnection connection = url.openConnection();
            if (connection instanceof HttpURLConnection) {
                HttpURLConnection http = (HttpURLConnection) connection;
                http.setRequestMethod("DELETE");
                int responseCode = http.getResponseCode();
                if (responseCode < 300) return true;
                if (responseCode == 404) return false;
                throw new IOException(http.getResponseMessage());
            } else return false;
        } catch (Exception e) {
            return false;
        }
    }
