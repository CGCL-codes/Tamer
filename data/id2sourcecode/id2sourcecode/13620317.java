    public Item doAddAttachment(String file, Item item) throws UnsupportedEncodingException, IOException {
        log(INFO, "Item id=" + item.getId());
        String sessionId = (String) RuntimeAccess.getInstance().getSession().getAttribute("SESSION_ID");
        DefaultHttpClient httpclient = new DefaultHttpClient();
        AddAttachmentRequest request = new AddAttachmentRequest();
        request.setSessionId(sessionId);
        request.setFile(file);
        request.setItem(item);
        XStream writer = new XStream();
        writer.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        writer.alias("AddAttachmentRequest", AddAttachmentRequest.class);
        XStream reader = new XStream();
        reader.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        reader.alias("AddAttachmentResponse", AddAttachmentResponse.class);
        String strRequest = URLEncoder.encode(reader.toXML(request), "UTF-8");
        HttpPost httppost = new HttpPost(MewitProperties.getMewitUrl() + "/resources/addAttachment?REQUEST=" + strRequest);
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = URLDecoder.decode(EntityUtils.toString(entity), "UTF-8");
            AddAttachmentResponse oResponse = (AddAttachmentResponse) reader.fromXML(result);
            log(INFO, "item id=" + oResponse.getItem().getId());
            for (Attachment a : oResponse.getItem().getFileRepository().getAttachments()) {
                log(INFO, "      Attachment:" + a.getFileName() + "  upload date:" + a.getUploadDate());
            }
            return oResponse.getItem();
        }
        return null;
    }
