    public java.net.HttpURLConnection getConnection(java.util.Map<String, String> getParams) throws IOException {
        String callURL = theURL;
        if (getParams.size() > 0) {
            StringBuilder args = new StringBuilder();
            boolean first = true;
            for (java.util.Map.Entry<String, String> p : getParams.entrySet()) {
                args.append(first ? '?' : '&');
                first = false;
                args.append(encode(p.getKey())).append('=').append(encode(p.getValue()));
            }
            callURL += args.toString();
        }
        java.net.HttpURLConnection conn;
        java.net.URL url = new java.net.URL(callURL);
        conn = (java.net.HttpURLConnection) url.openConnection();
        if (isFollowingRedirects != null) conn.setInstanceFollowRedirects(isFollowingRedirects.booleanValue());
        if (theConnectTimeout >= 0) conn.setConnectTimeout(theConnectTimeout);
        if (theReadTimeout >= 0) conn.setReadTimeout(theReadTimeout);
        if (conn instanceof javax.net.ssl.HttpsURLConnection) {
            javax.net.ssl.HttpsURLConnection sConn = (javax.net.ssl.HttpsURLConnection) conn;
            if (theSocketFactory != null) sConn.setSSLSocketFactory(theSocketFactory);
            if (theHostnameVerifier != null) sConn.setHostnameVerifier(theHostnameVerifier);
        }
        java.util.Map<String, String> cookies = theCookies;
        if (cookies != null && !cookies.isEmpty()) {
            StringBuilder cookie = new StringBuilder();
            boolean first = true;
            for (java.util.Map.Entry<String, String> c : cookies.entrySet()) {
                if (!first) cookie.append(", ");
                first = false;
                cookie.append(c.getKey()).append('=').append(c.getValue());
            }
            conn.setRequestProperty("Cookie", cookie.toString());
        }
        return conn;
    }
