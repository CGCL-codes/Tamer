    private static InputStream getTestXMLStream() throws IOException {
        URL url = new URL(JIRA_VUE_URL);
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("Cookie", JIRA_SFRAIZE_COOKIE);
        errout("Opening connection to " + url);
        conn.connect();
        errout("Getting InputStream...");
        InputStream in = conn.getInputStream();
        errout("Got " + Util.tags(in));
        errout("Getting headers...");
        Map<String, List<String>> headers = conn.getHeaderFields();
        errout("HEADERS:");
        for (Map.Entry<String, List<String>> e : headers.entrySet()) {
            errout(e.getKey() + ": " + e.getValue());
        }
        return in;
    }
