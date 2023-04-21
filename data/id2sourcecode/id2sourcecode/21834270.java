    private String getHttpResponse(String location) {
        String result = null;
        URL url = null;
        try {
            url = new URL(location);
        } catch (MalformedURLException e) {
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
        }
        if (url != null) {
            try {
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                String inputLine;
                int lineCount = 0;
                while ((lineCount < 10) && ((inputLine = in.readLine()) != null)) {
                    lineCount++;
                    result += "\n" + inputLine;
                }
                in.close();
                urlConn.disconnect();
            } catch (IOException e) {
                Log.e(Constants.LOG_TAG, e.getMessage(), e);
            }
        } else {
            Log.e(Constants.LOG_TAG, "url NULL");
        }
        return result;
    }
