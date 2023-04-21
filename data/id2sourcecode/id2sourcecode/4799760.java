    public MyUrl(String address) throws Exception {
        URL url = new URL(address);
        URLConnection url2 = url.openConnection();
        url2.setConnectTimeout(10000);
        url2.setReadTimeout(10000);
        this.url = url;
    }
