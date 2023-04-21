    private void openInputStream() {
        try {
            URL url = new URL(urlString);
            int retryCount = 0;
            int myTimeout = timeout;
            while (is == null) {
                retryCount++;
                try {
                    HttpURLConnection.setFollowRedirects(true);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(timeout);
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        System.err.println("HttpServer returned an error code (" + urlString + "): " + connection.getResponseCode());
                        connection = null;
                    }
                    if (connection != null) {
                        is = connection.getInputStream();
                        if (retryCount > 1) {
                            System.err.println("Retry successful");
                        }
                    }
                } catch (SocketTimeoutException e) {
                    System.err.println("Timeout (" + urlString + "): " + " " + e.toString());
                }
                myTimeout = myTimeout * 2;
                if (!tryVeryHard || (retryCount > 3)) {
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error connecting to http-Server (" + urlString + "): ");
            e.printStackTrace(System.err);
        }
        return;
    }
