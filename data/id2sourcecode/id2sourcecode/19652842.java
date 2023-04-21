    public int getStatusCode() {
        if (connection == null) try {
            connection = url.openConnection();
        } catch (IOException e) {
        }
        if (connection != null) {
            try {
                return ((HttpURLConnection) connection).getResponseCode();
            } catch (IOException e) {
                UpdateCore.warn("", e);
            }
        }
        return IStatusCodes.HTTP_OK;
    }
