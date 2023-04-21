    public void send(FeedbackData data) throws TransportException {
        try {
            URLConnection conn = (URLConnection) url.openConnection();
            String dataString = toXML(data);
            if (conn instanceof HttpURLConnection) {
                HttpURLConnection httpConnection = null;
                try {
                    httpConnection = (HttpURLConnection) conn;
                    httpConnection.setDoOutput(true);
                    httpConnection.setRequestMethod("POST");
                    httpConnection.setRequestProperty("Content-Length", Integer.toString(dataString.length()));
                    httpConnection.connect();
                    PrintWriter out = null;
                    try {
                        out = new PrintWriter(new OutputStreamWriter(httpConnection.getOutputStream()));
                        out.print(dataString);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new TransportException("Error sending data", e);
                    } finally {
                        IOUtilities.close(out);
                    }
                    int responseCode = httpConnection.getResponseCode();
                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        throw new TransportException("Bad HTTP server response: " + responseCode);
                    }
                } finally {
                    httpConnection.disconnect();
                }
            } else {
                throw new TransportException("Connection must be to an HTTP server");
            }
        } catch (TransportException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new TransportException("Error during data send", e);
        }
    }
