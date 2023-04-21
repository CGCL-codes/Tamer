    public static Map<String, String> prePostDiary(String verify) throws NetworkException {
        String url = HttpUtil.KAXIN_DAIRY_WRITE_URL + HttpUtil.KAIXIN_PARAM_VERIFY + verify;
        HttpClient client = HttpConfig.newInstance();
        HttpGet get = new HttpGet(url);
        HttpUtil.setHeader(get);
        try {
            HttpResponse response = client.execute(get);
            String html = HTTPUtil.toString(response.getEntity());
            return PictureAction.dealSelectOptions(html, "name=\"classid\"");
        } catch (Exception e) {
            e.printStackTrace();
            throw new NetworkException(e);
        }
    }
