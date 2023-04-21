    private void doGet(String urlString) throws Exception {
        System.out.println("-----");
        System.out.println("doGet: " + urlString);
        URL url = toURL(urlString);
        System.out.println("full url: " + url);
        HttpURLConnection conn = (HttpURLConnection) (url.openConnection());
        setCookie(conn);
        conn.setFollowRedirects(false);
        InputStream input = null;
        try {
            input = conn.getInputStream();
            parseCookie(conn);
            lastUrl = url;
            lastFetched = IoUtils.readFullyAsString(conn.getInputStream());
            System.out.println("New request cookie to : " + jSessionId);
        } finally {
            IoUtils.close(input);
        }
    }
