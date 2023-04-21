    public void connect(URL url) throws InterruptedException, Exception {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection instanceof HttpsURLConnection) {
                HttpsURLConnection secureConnection = (HttpsURLConnection) connection;
                secureConnection.setHostnameVerifier(new HostnameVerifier() {

                    public boolean verify(String urlHostName, javax.net.ssl.SSLSession session) {
                        if (Options.isDebug()) {
                            System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
                        }
                        return true;
                    }
                });
            }
            if (Options.isDebug()) {
                System.out.println("Connection  to control servlet created.");
            }
            connection.connect();
            if (Options.isDebug()) {
                System.out.println("Connection  to control servlet connected.");
            }
            int code = connection.getResponseCode();
            if (Options.isDebug()) {
                System.out.println("Response code from control servlet=" + code);
            }
            connection.disconnect();
            if (code == HttpURLConnection.HTTP_MOVED_TEMP) {
                String redirectLocation = connection.getHeaderField("location");
                URL redirectURL = new URL(redirectLocation);
                if (url.equals(redirectURL)) {
                    if (Options.isDebug()) {
                        System.out.println("Redirecting to the same URL! " + redirectLocation);
                    }
                    return;
                }
                if (Options.isDebug()) {
                    System.out.println("Follows redirect to " + redirectLocation);
                }
                connect(redirectURL);
            }
            return;
        } catch (IOException ioe) {
            if (Options.isDebug()) {
                ioe.printStackTrace();
            }
        }
    }
