    protected void doAuthentication() throws ClientProtocolException, IOException {
        if (loginUrl == null || user == null) {
            return;
        }
        String url = loginUrl + "?j_username=" + user + "&j_password=" + (password == null ? "" : password);
        DefaultHttpClient client = new DefaultHttpClient();
        try {
            HttpPost post = new HttpPost(url);
            if (logger.isDebugEnabled()) {
                logger.debug("Attempting login: request is " + post.toString());
            }
            HttpResponse hr = client.execute(post);
            StatusLine sl = hr.getStatusLine();
            switch(sl.getStatusCode()) {
                case HttpServletResponse.SC_OK:
                    break;
                case HttpServletResponse.SC_MOVED_TEMPORARILY:
                    Header h = hr.getFirstHeader("Location");
                    if (h != null && h.getValue().contains("loggedIn.html")) {
                        break;
                    }
                default:
                    logger.error("Login using url '" + url + "' failed: " + sl);
                    return;
            }
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            for (Cookie c : client.getCookieStore().getCookies()) {
                StringBuilder sb = (c instanceof BasicClientCookie2) ? sb2 : sb1;
                if (sb.length() > 0) {
                    sb.append("; ");
                }
                sb.append(c.getName());
                sb.append('=');
                sb.append(c.getValue());
            }
            if (sb1.length() > 0) {
                cookie = sb1.toString();
            }
            if (sb2.length() > 0) {
                cookie2 = sb2.toString();
            }
        } finally {
            client.getConnectionManager().shutdown();
        }
    }
