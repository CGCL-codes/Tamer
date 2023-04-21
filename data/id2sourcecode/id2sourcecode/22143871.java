    protected boolean linkIsToTorrent(URL url) {
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("HEAD");
            con.setConnectTimeout(10 * 1000);
            con.setReadTimeout(10 * 1000);
            String content_type = con.getContentType();
            log("Testing link " + url + " to see if torrent link -> content type=" + content_type);
            if (content_type.equalsIgnoreCase("application/x-bittorrent")) {
                return (true);
            }
            return (false);
        } catch (Throwable e) {
            return (false);
        }
    }
