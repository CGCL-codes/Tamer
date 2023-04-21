    public Item doAccept(String sessionId, Item item) throws UnsupportedEncodingException, IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        AcceptItemRequest request = new AcceptItemRequest();
        request.setItemID(item.getId());
        request.setSessionId(sessionId);
        XStream writer = new XStream();
        writer.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        writer.alias("AcceptItemRequest", AcceptItemRequest.class);
        XStream reader = new XStream();
        reader.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        reader.alias("AcceptItemResponse", AcceptItemResponse.class);
        String strRequest = URLEncoder.encode(reader.toXML(request), "UTF-8");
        HttpPost httppost = new HttpPost(MewitProperties.getMewitUrl() + "/resources/acceptItem?REQUEST=" + strRequest);
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = URLDecoder.decode(EntityUtils.toString(entity), "UTF-8");
            AcceptItemResponse oResponse = (AcceptItemResponse) reader.fromXML(result);
            return oResponse.getItem();
        }
        return null;
    }
