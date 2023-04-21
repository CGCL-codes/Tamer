    public static HttpClientStatus getRemoteCalendar(URL url, final String username, final String password, File outputFile) {
        if (username != null && password != null) {
            Authenticator.setDefault(new Authenticator() {

                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password.toCharArray());
                }
            });
        } else {
            Authenticator.setDefault(new Authenticator() {

                protected PasswordAuthentication getPasswordAuthentication() {
                    return null;
                }
            });
        }
        HttpURLConnection urlC = null;
        int totalRead = 0;
        try {
            urlC = (HttpURLConnection) url.openConnection();
            InputStream is = urlC.getInputStream();
            OutputStream os = new FileOutputStream(outputFile);
            DataInputStream dis = new DataInputStream(new BufferedInputStream(is));
            byte[] buf = new byte[4 * 1024];
            int bytesRead;
            while ((bytesRead = dis.read(buf)) != -1) {
                os.write(buf, 0, bytesRead);
                totalRead += bytesRead;
            }
            os.close();
            dis.close();
            urlC.disconnect();
            if (urlC.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                return new HttpClientStatus(HttpClientStatus.HTTP_STATUS_NOT_FOUND, "File not found on server");
            } else if (urlC.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                return new HttpClientStatus(HttpClientStatus.HTTP_STATUS_AUTH_REQUIRED, "Authorizaton required");
            } else if (urlC.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return new HttpClientStatus(HttpClientStatus.HTTP_STATUS_OTHER_ERROR, "HTTP Error" + " " + urlC.getResponseCode() + ": " + urlC.getResponseMessage());
            }
        } catch (IOException e1) {
            try {
                if (urlC.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                    return new HttpClientStatus(HttpClientStatus.HTTP_STATUS_NOT_FOUND, "File not found on server");
                } else if (urlC.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    return new HttpClientStatus(HttpClientStatus.HTTP_STATUS_AUTH_REQUIRED, "Authorizaton required");
                } else if (urlC.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return new HttpClientStatus(HttpClientStatus.HTTP_STATUS_OTHER_ERROR, "HTTP Error" + " " + +urlC.getResponseCode() + ": " + urlC.getResponseMessage());
                } else {
                    return new HttpClientStatus(HttpClientStatus.HTTP_STATUS_OTHER_ERROR, "HTTP I/O Exception" + ":", e1);
                }
            } catch (IOException e2) {
                e2.printStackTrace();
                return new HttpClientStatus(HttpClientStatus.HTTP_STATUS_OTHER_ERROR, "HTTP I/O Error" + ": " + e1.getMessage(), e1);
            }
        }
        return new HttpClientStatus(HttpClientStatus.HTTP_STATUS_SUCCESS, outputFile);
    }
