    public void doRetrievePassword(String email) throws UnsupportedEncodingException, IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        RetrievePasswordRequest request = new RetrievePasswordRequest();
        request.setEmail(email);
        XStream writer = new XStream();
        writer.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        writer.alias("RetrievePasswordRequest", RetrievePasswordRequest.class);
        XStream reader = new XStream();
        reader.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        reader.alias("RetrievePasswordResponse", RetrievePasswordResponse.class);
        String strRequest = URLEncoder.encode(reader.toXML(request), "UTF-8");
        log(INFO, strRequest);
        HttpPost httppost = new HttpPost(MewitProperties.getMewitUrl() + "/resources/retrivePassword?REQUEST=" + strRequest);
        HttpResponse httpresponse = httpclient.execute(httppost);
        HttpEntity entity = httpresponse.getEntity();
        if (entity != null) {
            String result = URLDecoder.decode(EntityUtils.toString(entity), "UTF-8");
            log(INFO, result);
            RetrievePasswordResponse oResponse = (RetrievePasswordResponse) reader.fromXML(result);
        }
    }
