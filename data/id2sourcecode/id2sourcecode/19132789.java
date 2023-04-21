    private static String unredirect(String uri) throws IOException {
        if (!isRedirector(uri)) {
            return uri;
        }
        HttpUriRequest head = new HttpHead(uri);
        AndroidHttpClient client = AndroidHttpClient.newInstance(null);
        HttpResponse response = client.execute(head);
        int status = response.getStatusLine().getStatusCode();
        if (status == 301 || status == 302) {
            Header redirect = response.getFirstHeader("Location");
            if (redirect != null) {
                String location = redirect.getValue();
                if (location != null) {
                    return location;
                }
            }
        }
        return uri;
    }
