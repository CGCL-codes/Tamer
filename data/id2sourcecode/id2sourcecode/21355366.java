    public Item doGiveFeedback(String sessionId, Item item, String feedback) throws UnsupportedEncodingException, IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        GiveFeedbackRequest request = new GiveFeedbackRequest();
        request.setItemID(item.getId());
        request.setSessionId(sessionId);
        request.setFeedback(feedback);
        XStream writer = new XStream();
        writer.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        writer.alias("GiveFeedbackRequest", GiveFeedbackRequest.class);
        XStream reader = new XStream();
        reader.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        reader.alias("GiveFeedbackResponse", GiveFeedbackResponse.class);
        String strRequest = URLEncoder.encode(reader.toXML(request), "UTF-8");
        HttpPost httppost = new HttpPost(MewitProperties.getMewitUrl() + "/resources/giveFeedback?REQUEST=" + strRequest);
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = URLDecoder.decode(EntityUtils.toString(entity), "UTF-8");
            GiveFeedbackResponse oResponse = (GiveFeedbackResponse) reader.fromXML(result);
            return oResponse.getItem();
        }
        return null;
    }
