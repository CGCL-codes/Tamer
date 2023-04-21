    private static void fileUpload() throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(postURL);
        file = new File("h:/UploadingdotcomUploaderPlugin.java");
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        ContentBody cbFile = new FileBody(file);
        mpEntity.addPart("emai", new StringBody("Free"));
        mpEntity.addPart("upload_range", new StringBody("1"));
        mpEntity.addPart("upfile_0", cbFile);
        httppost.setEntity(mpEntity);
        System.out.println("executing request " + httppost.getRequestLine());
        System.out.println("Now uploading your file into MegaShare.com");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        uploadresponse = response.getLastHeader("Location").getValue();
        System.out.println("Upload response : " + uploadresponse);
        System.out.println(response.getStatusLine());
        httpclient.getConnectionManager().shutdown();
    }
