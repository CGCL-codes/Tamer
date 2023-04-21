    private static void fileUpload() throws IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        System.out.println(postURL);
        System.out.println(ucookie);
        System.out.println(uid);
        HttpPost httppost = new HttpPost(postURL);
        httppost.setHeader("Cookie", ucookie);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        File f = new File("h:/Rock Lee.jpg");
        reqEntity.addPart("UPLOAD_IDENTIFIER", new StringBody(uid));
        reqEntity.addPart("u", new StringBody(ucookie));
        FileBody bin = new FileBody(f);
        reqEntity.addPart("file_0", bin);
        reqEntity.addPart("service_1", new StringBody("1"));
        reqEntity.addPart("service_16", new StringBody("1"));
        reqEntity.addPart("service_7", new StringBody("1"));
        reqEntity.addPart("service_17", new StringBody("1"));
        reqEntity.addPart("service_9", new StringBody("1"));
        reqEntity.addPart("service_10", new StringBody("1"));
        reqEntity.addPart("remember_1", new StringBody("1"));
        reqEntity.addPart("remember_16", new StringBody("1"));
        reqEntity.addPart("remember_7", new StringBody("1"));
        reqEntity.addPart("remember_17", new StringBody("1"));
        reqEntity.addPart("remember_9", new StringBody("1"));
        reqEntity.addPart("remember_10", new StringBody("1"));
        httppost.setEntity(reqEntity);
        System.out.println("Now uploading your file into multiupload.com. Please wait......................");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
            uploadresponse = EntityUtils.toString(resEntity);
            System.out.println("Response :\n" + uploadresponse);
            uploadresponse = parseResponse(uploadresponse, "\"downloadid\":\"", "\"");
            downloadlink = "http://www.multiupload.com/" + uploadresponse;
            System.out.println("Download link : " + downloadlink);
        }
    }
