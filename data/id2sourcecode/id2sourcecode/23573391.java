    public boolean openConnection() {
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            connection.connect();
            outputStream = new DataOutputStream(connection.getOutputStream());
        } catch (Exception e) {
            if (D) e.printStackTrace();
            return false;
        }
        return true;
    }
