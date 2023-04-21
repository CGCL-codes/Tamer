    public void connect() throws IOException, MalformedCookieException {
        if (bConnectMethodAlreadyCalled) {
            throw new IllegalStateException("No can do.");
        }
        bConnectMethodAlreadyCalled = true;
        int code;
        URL url;
        huc.setInstanceFollowRedirects(false);
        if (!cj.isEmpty()) {
            client.setCookies(huc, cj);
        }
        is = huc.getInputStream();
        cj.addAll(client.getCookies(huc));
        while ((code = huc.getResponseCode()) != successCode && maxRedirects > 0) {
            if (code < 300 || code > 399) {
                throw new IOException("Can't deal with this response code (" + code + ").");
            }
            is.close();
            is = null;
            url = new URL(huc.getHeaderField("location"));
            huc.disconnect();
            huc = null;
            huc = (HttpURLConnection) url.openConnection();
            client.setCookies(huc, cj);
            huc.setInstanceFollowRedirects(false);
            huc.connect();
            is = huc.getInputStream();
            cj.addAll(client.getCookies(huc));
            maxRedirects--;
        }
        if (maxRedirects <= 0 && code != successCode) {
            throw new IOException("Max redirects exhausted.");
        }
        bConnected = true;
    }
