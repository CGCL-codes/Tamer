    public Item doNegotiateDeadline(String itemId, String reason, Date newDeadline) throws UnsupportedEncodingException, IOException {
        log(INFO, "Negotiate deadline: Item id=" + itemId);
        String sessionId = (String) RuntimeAccess.getInstance().getSession().getAttribute("SESSION_ID");
        DefaultHttpClient httpclient = new DefaultHttpClient();
        NegotiateDeadlineRequest request = new NegotiateDeadlineRequest();
        request.setItemID(itemId);
        request.setSessionId(sessionId);
        request.setReasonForNegotiationOfDeadline(reason);
        request.setNewProposedDeadline(newDeadline);
        XStream writer = new XStream();
        writer.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        writer.alias("NegotiateDeadlineRequest", NegotiateDeadlineRequest.class);
        XStream reader = new XStream();
        reader.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        reader.alias("NegotiateDeadlineResponse", NegotiateDeadlineResponse.class);
        String strRequest = URLEncoder.encode(reader.toXML(request), "UTF-8");
        HttpPost httppost = new HttpPost(MewitProperties.getMewitUrl() + "/MEWIT/resources/negotiateDeadline?REQUEST=" + strRequest);
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = URLDecoder.decode(EntityUtils.toString(entity), "UTF-8");
            NegotiateDeadlineResponse oResponse = (NegotiateDeadlineResponse) reader.fromXML(result);
            return oResponse.getItem();
        }
        return null;
    }
