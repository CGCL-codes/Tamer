    private Object postRequest(URL url, String contentType, int output, PassingObject po, String body, int input) {
        try {
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            httpConnection.setUseCaches(false);
            httpConnection.setRequestProperty("Content-Type", contentType);
            if (session != null) httpConnection.addRequestProperty("Cookie", session);
            ((HttpURLConnection) httpConnection).setChunkedStreamingMode(1024);
            if (output == OUT_BEAN) outputObject(httpConnection, po); else if (output == OUT_PARAMETERS) ouputParameters(httpConnection, body);
            if (session == null) getSession(httpConnection);
            if (input == INPUT_BEAN) return inputObject(httpConnection); else if (input == INPUT_FILE) return inputFile(httpConnection);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
