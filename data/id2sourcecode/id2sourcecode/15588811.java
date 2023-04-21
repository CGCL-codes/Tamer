    private void post(String server, String post_data) throws IOException {
        URL url = new URL(server.replaceAll("\\\\", "/"));
        if (proxy != null) {
            Authenticator.setDefault(this);
            Properties systemProperties = System.getProperties();
            systemProperties.setProperty("http.proxySet", "true");
            systemProperties.setProperty("http.proxyHost", proxy);
            systemProperties.setProperty("http.proxyPort", port);
        }
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        if (post_data != null) {
            if (post_data.length() > 0) {
                urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConn.setRequestProperty("Content-Length", "" + post_data.length());
                DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
                dos.writeBytes(post_data);
                dos.writeBytes("\r\n");
                dos.flush();
                dos.close();
            }
        }
        response_code = urlConn.getResponseCode();
        DataInputStream dis = new DataInputStream(urlConn.getInputStream());
        IOFunctions.WaitForData(dis, TIMEOUT);
        if (urlConn.getContentType().startsWith("text")) {
            String s = "";
            StringBuffer str = new StringBuffer();
            while ((s = dis.readLine()) != null) {
                str.append(s + "\n");
            }
            if (str.length() > 0) {
                str.delete(str.length() - 1, str.length());
            }
            text_response = str.toString();
        } else {
            int bytes_remaining = urlConn.getContentLength();
            int bytes_read = 0;
            data_response = new byte[urlConn.getContentLength()];
            while (bytes_remaining > 0) {
                try {
                    int len = dis.read(data_response, bytes_read, bytes_remaining);
                    if (len < 0) {
                        break;
                    }
                    bytes_read += len;
                    bytes_remaining -= len;
                } catch (java.lang.IndexOutOfBoundsException ex) {
                    ex.printStackTrace();
                }
            }
        }
        dis.close();
    }
