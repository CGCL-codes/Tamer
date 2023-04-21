    private static void fileUpload() throws IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(postURL);
        httppost.setHeader("Cookie", sidcookie + ";" + timecookie + ";" + cachecookie + ";" + ucookie);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        reqEntity.addPart("Filename", new StringBody(file.getName()));
        reqEntity.addPart("folder_id", new StringBody("0"));
        reqEntity.addPart("SID", new StringBody(sid));
        reqEntity.addPart("file", new StringBody(fileID));
        FileBody bin = new FileBody(file);
        reqEntity.addPart("file", bin);
        reqEntity.addPart("upload", new StringBody("Submit Query"));
        httppost.setEntity(reqEntity);
        System.out.println("Now uploading your file into uploading.com. Please wait......................");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
            uploadresponse = EntityUtils.toString(resEntity);
            System.out.println("PAGE :" + uploadresponse);
            uploadresponse = parseResponse(uploadresponse, "answer\":\"", "\"");
        }
    }
