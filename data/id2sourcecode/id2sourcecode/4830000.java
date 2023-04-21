    private void doPost(String urlString, String... pairs) throws Exception {
        System.out.println("-----");
        System.out.println("doPost: " + urlString);
        if (pairs.length % 2 != 0) {
            throw new IllegalArgumentException("Pairs must come in pairs.");
        }
        URL url = toURL(urlString);
        System.out.println("full URL: " + url);
        for (int i = 0; i < pairs.length; i += 2) {
            System.out.println(pairs[i] + "=" + pairs[i + 1]);
        }
        HttpURLConnection conn = (HttpURLConnection) (url.openConnection());
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        setCookie(conn);
        OutputStreamWriter wr = null;
        try {
            wr = new OutputStreamWriter(conn.getOutputStream());
            if (pairs.length > 0) {
                wr.write(URLEncoder.encode(pairs[0], "UTF-8"));
                wr.write('=');
                wr.write(URLEncoder.encode(pairs[1], "UTF-8"));
                for (int i = 2; i < pairs.length; i += 2) {
                    wr.write('&');
                    wr.write(URLEncoder.encode(pairs[i], "UTF-8"));
                    wr.write('=');
                    wr.write(URLEncoder.encode(pairs[i + 1], "UTF-8"));
                }
            }
            wr.flush();
        } finally {
            IoUtils.close(wr);
        }
        InputStream input = null;
        try {
            input = conn.getInputStream();
            lastUrl = url;
            lastFetched = IoUtils.readFullyAsString(conn.getInputStream());
            parseCookie(conn);
            System.out.println("Response: " + conn.getResponseCode());
            System.out.println("Request Cookie is now: " + jSessionId);
            System.out.println(lastFetched);
        } finally {
            IoUtils.close(input);
        }
    }
