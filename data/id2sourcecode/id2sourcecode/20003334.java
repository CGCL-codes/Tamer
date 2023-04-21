    public URLConnection openConnection(Link link) throws IOException {
        int method = link.getMethod();
        URL url;
        switch(method) {
            case Link.GET:
                url = link.getPageURL();
                break;
            case Link.POST:
                url = link.getServiceURL();
                break;
            default:
                throw new IOException("Unknown HTTP method " + link.getMethod());
        }
        URLConnection conn = url.openConnection();
        DownloadParameters dp = link.getDownloadParameters();
        if (dp != null) {
            conn.setAllowUserInteraction(dp.getInteractive());
            conn.setUseCaches(dp.getUseCaches());
            String userAgent = dp.getUserAgent();
            if (userAgent != null) {
                conn.setRequestProperty("User-Agent", userAgent);
            }
            String acceptLanguage = dp.getAcceptLanguage();
            if (acceptLanguage != null) {
                conn.setRequestProperty("Accept-Language", acceptLanguage);
            }
            String types = dp.getAcceptedMIMETypes();
            if (types != null) {
                conn.setRequestProperty("accept", types);
            }
        }
        if (method == Link.POST) {
            if (conn instanceof HttpURLConnection) ((HttpURLConnection) conn).setRequestMethod("POST");
            String query = link.getQuery();
            if (query.startsWith("?")) query = query.substring(1);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-length", String.valueOf(query.length()));
            PrintStream out = new PrintStream(conn.getOutputStream());
            out.print(query);
            out.flush();
        }
        conn.connect();
        return conn;
    }
