    public static VerifyImage fetchVerifyImage(User user, LocaleSetting setting, String alg, String type) throws IOException, ParseException {
        String picurl = setting.getNodeText("/config/servers/get-pic-code");
        if (picurl == null) picurl = FetionConfig.getString("server.verify-pic-uri");
        picurl += "?algorithm=" + alg;
        URL url = new URL(picurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.addRequestProperty("User-Agent", "IIC2.0/PC " + FetionClient.PROTOCOL_VERSION);
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            Element e = XMLHelper.build(conn.getInputStream());
            Element pic = XMLHelper.find(e, "/results/pic-certificate");
            String pid = pic.getAttributeValue("id");
            String code = pic.getAttributeValue("pic");
            byte[] base64Data = code.getBytes();
            byte[] bytes = Base64.decodeBase64(base64Data);
            return new VerifyImage(pid, bytes, alg, type);
        } else {
            throw new IOException("Http response is not OK. code=" + conn.getResponseCode());
        }
    }
