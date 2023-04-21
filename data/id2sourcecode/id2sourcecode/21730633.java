    public JobIndexPage(String urlStr) {
        try {
            URL url = new URL(urlStr);
            URLConnection urlConn = url.openConnection();
            urlConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 4.0)");
            urlConn.setRequestProperty("Accept-Language", "en-us");
            this.content = (String) urlConn.getContent();
        } catch (IOException e) {
            System.out.println("ALERT: Cannot access \"" + urlStr + "\". Maybe the URL is not valid.");
            System.exit(1);
        }
        this.jobPageLinkHtmlList = extractJobPageLinkHtmlList();
    }
