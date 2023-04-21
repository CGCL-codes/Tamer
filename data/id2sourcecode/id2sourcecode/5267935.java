    protected static HttpURLConnection createURLConnection(String module) throws Exception {
        String url = DPDesktopDataLocal.getServiceURL();
        HttpURLConnection u = (HttpURLConnection) new URL(url + getAuthString() + "&module=" + module).openConnection();
        return u;
    }
