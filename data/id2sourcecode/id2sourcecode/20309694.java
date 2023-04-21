    public void fileUpload() throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        file = new File("C:\\Documents and Settings\\dinesh\\Desktop\\ImageShackUploaderPlugin.java");
        HttpPost httppost = new HttpPost(postURL);
        httppost.setHeader("Cookie", langcookie + ";" + sessioncookie + ";" + mailcookie + ";" + namecookie + ";" + rolecookie + ";" + msgreadcookie + ";" + msgcheckcookie + ";");
        MultipartEntity mpEntity = new MultipartEntity();
        ContentBody cbFile = new FileBody(file);
        mpEntity.addPart("files[]", cbFile);
        httppost.setEntity(mpEntity);
        System.out.println("Now uploading your file into filesonic...........................");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        System.out.println(response.getStatusLine());
        uploadresponse = response.getLastHeader("Location").getValue();
        System.out.println("Upload response URL : " + uploadresponse);
        uploadresponse = getData(uploadresponse);
        if (uploadresponse.contains("File was successfully uploaded")) {
            System.out.println("File was successfully uploaded :)");
        } else {
            throw new Exception("There might be a problem with your internet connecivity or server error. Please try after some time. :(");
        }
    }
