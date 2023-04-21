    private String requestLongURL(String url) {
        log.debug("requestLongURL({})", url);
        HttpHead head = new HttpHead(url);
        try {
            HttpResponse response = httpClient.execute(head);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode < 200 || statusCode > 302) {
                log.error("URL service {} returned {}, returning unchanged value.", url, statusCode);
                return url;
            }
            Header header = response.getFirstHeader("Location");
            if (header != null) {
                String location = header.getValue();
                if (location != null) {
                    return location;
                }
            }
        } catch (IOException e) {
            log.error("io exception resolving url", e);
            head.abort();
        }
        return url;
    }
