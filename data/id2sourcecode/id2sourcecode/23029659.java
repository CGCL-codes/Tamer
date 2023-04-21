    private int execute(String query, String userID) {
        int result = SearchResult.RESULT_UNKNOWN;
        BufferedReader reader = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(query);
            connection = (HttpURLConnection) url.openConnection();
            configureGoogleConnection(connection, userID);
            connection.connect();
            connection.getResponseCode();
            String line;
            final StringBuilder builder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            JSONObject json = new JSONObject(builder.toString());
            result = analyseResponse(json);
        } catch (Exception e) {
            result = SearchResult.RESULT_UNKNOWN;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        return result;
    }
