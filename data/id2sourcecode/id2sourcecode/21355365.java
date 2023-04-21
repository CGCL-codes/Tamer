    public Item doGiveCommentsOnFeedback(String sessionId, Item item, String comments, boolean approved) throws UnsupportedEncodingException, IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        GiveCommentsOnFeedbackRequest request = new GiveCommentsOnFeedbackRequest();
        request.setItemID(item.getId());
        request.setSessionId(sessionId);
        request.setComments(comments);
        request.setApproved(approved);
        XStream writer = new XStream();
        writer.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        writer.alias("GiveCommentsOnFeedbackRequest", GiveCommentsOnFeedbackRequest.class);
        XStream reader = new XStream();
        reader.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        reader.alias("GiveCommentsOnFeedbackResponse", GiveCommentsOnFeedbackResponse.class);
        String strRequest = URLEncoder.encode(reader.toXML(request), "UTF-8");
        HttpPost httppost = new HttpPost(MewitProperties.getMewitUrl() + "/resources/giveCommentsOnFeedback?REQUEST=" + strRequest);
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = URLDecoder.decode(EntityUtils.toString(entity), "UTF-8");
            GiveCommentsOnFeedbackResponse oResponse = (GiveCommentsOnFeedbackResponse) reader.fromXML(result);
            return oResponse.getItem();
        }
        return null;
    }
