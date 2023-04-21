    public Boolean doModifyPassword(String oldPassword, String newPassword) throws UnsupportedEncodingException, IOException {
        String sessionId = (String) RuntimeAccess.getInstance().getSession().getAttribute("SESSION_ID");
        DefaultHttpClient httpclient = new DefaultHttpClient();
        ModifyPasswordRequest request = new ModifyPasswordRequest();
        request.setSessionId(sessionId);
        request.setOldPassword(oldPassword);
        request.setNewPassword(newPassword);
        XStream writer = new XStream();
        writer.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        writer.alias("ModifyPasswordRequest", ModifyPasswordRequest.class);
        XStream reader = new XStream();
        reader.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        reader.alias("ModifyPasswordResponse", ModifyPasswordResponse.class);
        String strRequest = URLEncoder.encode(reader.toXML(request), "UTF-8");
        HttpPost httppost = new HttpPost(MewitProperties.getMewitUrl() + "/resources/modifyPassword?REQUEST=" + strRequest);
        HttpResponse httpresponse = httpclient.execute(httppost);
        HttpEntity entity = httpresponse.getEntity();
        if (entity != null) {
            String result = URLDecoder.decode(EntityUtils.toString(entity), "UTF-8");
            ModifyPasswordResponse modifyPasswordResponse = (ModifyPasswordResponse) reader.fromXML(result);
            if (modifyPasswordResponse.getErrors() != null) {
                return Boolean.FALSE;
            } else {
                return Boolean.TRUE;
            }
        }
        return null;
    }
