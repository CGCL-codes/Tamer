    synchronized void poll(String send) throws IOException {
        URLConnection urlTemp = pollingURL.openConnection();
        if (!(urlTemp instanceof HttpURLConnection)) {
            System.out.println("URL ist not a HTTP URL");
            disconnect();
            return;
        }
        HttpURLConnection urlConn = (HttpURLConnection) urlTemp;
        urlConn.setRequestMethod("POST");
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setAllowUserInteraction(true);
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        OutputStream out = urlConn.getOutputStream();
        if (sessionCookie == null) {
            out.write("0,".getBytes("UTF8"));
        } else {
            out.write((sessionCookie + ",").getBytes("UTF8"));
        }
        if (send != null) {
            out.write(send.getBytes("UTF8"));
        }
        out.flush();
        out.close();
        InputStream in = urlConn.getInputStream();
        sessionCookie = urlConn.getHeaderField("Set-Cookie");
        if (sessionCookie != null) {
            if (sessionCookie.substring(0, 3).equals("ID=")) {
                sessionCookie = sessionCookie.substring(3);
            }
            int index = sessionCookie.indexOf(';');
            if (index > 0) {
                sessionCookie = sessionCookie.substring(0, index - 1);
            }
        }
        if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            System.out.println("HTTP response code is: " + (new Integer(urlConn.getResponseCode())).toString());
            disconnect();
        }
        if (sessionCookie.endsWith(":0")) {
            System.out.println("Got error cookie: " + sessionCookie);
            disconnect();
        } else {
            System.out.println("Got session cookie: " + sessionCookie);
        }
        long bytesReceived = 0;
        int count;
        byte[] b = new byte[1024];
        while (-1 != (count = in.read(b, 0, 1024))) {
            if (count > 0) {
                String resp = new String(b, 0, count, "UTF8");
                bytesReceived += count;
            } else {
                try {
                    Thread.sleep(1000);
                } catch (Exception ie) {
                }
            }
        }
        in.close();
        int tenthOfInterval = pollingInterval / 10;
        if (bytesReceived > 2) {
            pollingInterval -= tenthOfInterval;
        } else {
            pollingInterval += tenthOfInterval;
        }
        if (pollingInterval > maxPollingInterval) {
            pollingInterval = maxPollingInterval;
        }
        if (pollingInterval < minPollingInterval) {
            pollingInterval = minPollingInterval;
        }
    }
