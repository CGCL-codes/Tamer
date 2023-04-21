    public static String home() throws NetworkException {
        HttpClient client = HttpUtil.newInstance();
        HttpGet get = new HttpGet(HttpUtil.KAIXIN_URL);
        HttpUtil.setHeader(get);
        try {
            HttpResponse response = client.execute(get);
            if (HTTPUtil.isHttp200(response)) {
                String ret = HTTPUtil.toString(response.getEntity());
                ret = HTMLUtil.findStr(ret, HttpUtil.KAIXIN_LOGIN_URL, "\"");
                ret = ret.replace("&amp;", "&");
                return ret;
            } else throw new NetworkException();
        } catch (Exception e) {
            e.printStackTrace();
            throw new NetworkException(e);
        }
    }
