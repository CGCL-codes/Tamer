    private void performRequest(final String urlString, byte[] postData, String postFile, boolean saveResponse, String responseFileName) throws IOException {
        HttpURLConnection connection = openConnection(urlString);
        Log.i(LOG_TAG, "performRequest - connection is " + connection);
        if (connection != null) {
            try {
                if (postData != null) {
                    writePostData(connection, postData);
                } else if (postFile != null) {
                    writePostFile(connection, postFile);
                }
                final int responseCode = connection.getResponseCode();
                Log.i(LOG_TAG, "performRequest - responseCode is " + responseCode);
                final String responseType = (responseCode == HttpURLConnection.HTTP_OK) ? getResponseType(connection) : "";
                Log.i(LOG_TAG, "performRequest - responseType is " + responseType);
                if (saveResponse) {
                    final String path = (responseCode == HttpURLConnection.HTTP_OK) ? saveResponseContent(connection, responseFileName, responseType) : "";
                    if (isaService) {
                        formservice.runOnSvcThread(new Runnable() {

                            @Override
                            public void run() {
                                GotFile(urlString, responseCode, responseType, path);
                            }
                        });
                    } else {
                        form.$context().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                GotFile(urlString, responseCode, responseType, path);
                            }
                        });
                    }
                } else {
                    final String responseContent = (responseCode == HttpURLConnection.HTTP_OK) ? getResponseContent(connection) : "";
                    if (isaService) {
                        formservice.runOnSvcThread(new Runnable() {

                            @Override
                            public void run() {
                                GotText(urlString, responseCode, responseType, responseContent);
                            }
                        });
                    } else {
                        form.$context().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                GotText(urlString, responseCode, responseType, responseContent);
                            }
                        });
                    }
                }
            } finally {
                connection.disconnect();
            }
        }
    }
