    private URLConnection openConnection(URL url) throws IOException {
        if (this.force_no_proxy) {
            return Java15Utils.openConnectionForceNoProxy(url);
        } else {
            return url.openConnection();
        }
    }
