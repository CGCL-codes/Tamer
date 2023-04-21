    private boolean init() {
        try {
            urlConnection = (HttpURLConnection) endpoint.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            connOpen = true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
