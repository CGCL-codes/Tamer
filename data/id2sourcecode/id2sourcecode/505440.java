    public static String showGetVerificationcCode(String url, Map<String, String> header, String fileUrl) throws ClientProtocolException, IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        if (null != header && header.size() > 0) {
            get.setHeaders(BaiduUtils.assemblyHeader(header));
        }
        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        InputStream in = entity.getContent();
        int temp = 0;
        File file = new File(fileUrl);
        FileOutputStream out = new FileOutputStream(file);
        while ((temp = in.read()) != -1) {
            out.write(temp);
        }
        in.close();
        out.close();
        return BaiduUtils.assemblyCookie(client.getCookieStore().getCookies());
    }
