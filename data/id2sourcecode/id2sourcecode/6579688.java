    public static void main(String[] args) throws Exception {
        String verify = "2538938_2538938_1301470122_f4df630273553820bea083ae0f52d4ba_kx";
        String start = "0";
        HttpClient client = HttpConfig.newInstance();
        String url = HttpUtil.KAIXIN_STATUS_URL + HttpUtil.KAIXIN_PARAM_UID + LoginAction.uid(verify);
        url += "&" + HttpUtil.KAIXIN_PARAM_VERIFY + verify;
        url += "&" + HttpUtil.KAIXIN_PRRAM_PSTART + start;
        HttpGet get = new HttpGet(url);
        HttpUtil.setHeader(get);
        try {
            HttpResponse response = client.execute(get);
            String html = HTTPUtil.toString(response.getEntity());
            System.out.println(html);
            fromHtml(html);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NetworkException(e);
        }
    }
