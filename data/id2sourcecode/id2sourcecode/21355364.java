    public Item doReject(String sessionId, Item item, String reason) throws UnsupportedEncodingException, IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        RejectItemRequest request = new RejectItemRequest();
        request.setItemID(item.getId());
        request.setSessionId(sessionId);
        request.setReason(reason);
        XStream writer = new XStream();
        writer.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        writer.alias("RejectItemRequest", RejectItemRequest.class);
        XStream reader = new XStream();
        reader.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        reader.alias("RejectItemResponse", RejectItemResponse.class);
        String strRequest = URLEncoder.encode(reader.toXML(request), "UTF-8");
        HttpPost httppost = new HttpPost(MewitProperties.getMewitUrl() + "/resources/rejectItem?REQUEST=" + strRequest);
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = URLDecoder.decode(EntityUtils.toString(entity), "UTF-8");
            RejectItemResponse oResponse = (RejectItemResponse) reader.fromXML(result);
            return oResponse.getItem();
        }
        return null;
    }
