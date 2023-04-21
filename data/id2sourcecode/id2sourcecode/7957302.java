    public static Picture detailPicture(String url) throws NetworkException {
        HttpClient client = HttpConfig.newInstance();
        HttpGet get = new HttpGet(url);
        HttpUtil.setHeader(get);
        try {
            HttpResponse response = client.execute(get);
            String html = HTTPUtil.toString(response.getEntity());
            return parsePicture(html);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NetworkException(e);
        }
    }
