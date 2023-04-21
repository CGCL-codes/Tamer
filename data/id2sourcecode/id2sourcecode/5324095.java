    public static void detailRepaste(String verify, Repaste r, int start) throws NetworkException {
        HttpClient client = HttpConfig.newInstance();
        String url = HttpUtil.KAIXIN_REPASTE_DETAIL_URL + HttpUtil.KAIXIN_PARAM_UID + r.getUid();
        url += "&" + HttpUtil.KAIXIN_PARAM_URPID + r.getUrpid();
        url += "&" + HttpUtil.KAIXIN_PARAM_VERIFY + verify;
        url += "&" + HttpUtil.KAIXIN_PRRAM_PSTART + start;
        HttpGet get = new HttpGet(url);
        HttpUtil.setHeader(get);
        try {
            HttpResponse response = client.execute(get);
            String html = HTTPUtil.toString(response.getEntity());
            if (Repaste.repasteContent(html, r)) {
                detailRepaste(verify, r, start + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NetworkException(e);
        }
    }
