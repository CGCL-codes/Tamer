    public static Map<String, String> preUploadPicture(String verify) throws NetworkException {
        String url = HttpUtil.KAIXIN_PIC_UP_URL + HttpUtil.KAIXIN_PARAM_VERIFY + verify;
        HttpClient client = HttpConfig.newInstance();
        HttpGet get = new HttpGet(url);
        HttpUtil.setHeader(get);
        try {
            HttpResponse response = client.execute(get);
            String html = HTTPUtil.toString(response.getEntity());
            return dealSelectOptions(html, "name=\"albumid\"");
        } catch (Exception e) {
            e.printStackTrace();
            throw new NetworkException(e);
        }
    }
