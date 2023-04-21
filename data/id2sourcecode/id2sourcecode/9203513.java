    public void writeCookies(URLConnection conn, Map<String, Map<String, String>> cookies) {
        URL url = conn.getURL();
        String domain = getDomainFromUrl(url);
        String path = url.getPath();
        Map<String, Map<String, String>> domainStore = (cookies != null ? Cloner.clone(cookies) : STORE.get(domain));
        if (domainStore == null) return;
        StringBuffer cookieStringBuffer = new StringBuffer();
        Iterator<String> cookieNames = domainStore.keySet().iterator();
        while (cookieNames.hasNext()) {
            String cookieName = cookieNames.next();
            Map<String, String> cookie = domainStore.get(cookieName);
            if (comparePaths(cookie.get(PATH), path) && isNotExpired(cookie.get(EXPIRES))) {
                cookieStringBuffer.append(cookieName);
                cookieStringBuffer.append("=");
                cookieStringBuffer.append(cookie.get(cookieName));
                if (cookieNames.hasNext()) {
                    cookieStringBuffer.append(SET_COOKIE_SEPARATOR);
                }
            }
        }
        try {
            conn.setRequestProperty(COOKIE, cookieStringBuffer.toString());
        } catch (java.lang.IllegalStateException ise) {
            throw new Error("Illegal State! Cookies cannot be set on a " + "URLConnection that is already connected. Only " + "call writeCookies(java.net.URLConnection) " + "AFTER calling java.net.URLConnection.connect().");
        }
    }
