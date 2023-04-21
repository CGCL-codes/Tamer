    public static List<Repaste> listRepaste(String verify, String uid, int start) throws NetworkException {
        HttpClient client = HttpConfig.newInstance();
        String url = HttpUtil.KAIXIN_REPASTES_URL + HttpUtil.KAIXIN_PARAM_UID + uid;
        url += "&" + HttpUtil.KAIXIN_PARAM_VERIFY + verify;
        url += "&" + HttpUtil.KAIXIN_PRRAM_PPAGE + start;
        HttpGet get = new HttpGet(url);
        HttpUtil.setHeader(get);
        try {
            HttpResponse response = client.execute(get);
            String html = HTTPUtil.toString(response.getEntity());
            return parseRepaste(html);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NetworkException(e);
        }
    }
