    public static List<Status> listStatus(String verify, int start) throws NetworkException {
        HttpClient client = HttpConfig.newInstance();
        String url = HttpUtil.KAIXIN_STATUS_URL + HttpUtil.KAIXIN_PARAM_UID + LoginAction.uid(verify);
        url += "&" + HttpUtil.KAIXIN_PARAM_VERIFY + verify;
        url += "&" + HttpUtil.KAIXIN_PRRAM_PSTART + start;
        HttpGet get = new HttpGet(url);
        HttpUtil.setHeader(get);
        try {
            HttpResponse response = client.execute(get);
            String html = HTTPUtil.toString(response.getEntity());
            return parseStatus(html);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NetworkException(e);
        }
    }
