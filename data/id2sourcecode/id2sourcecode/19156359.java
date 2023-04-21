    public static void fileUpload() throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        file = new File("C:\\Documents and Settings\\dinesh\\Desktop\\GeDotttUploaderPlugin.java");
        HttpPost httppost = new HttpPost("https://docs.zoho.com/uploadsingle.do?isUploadStatus=false&folderId=0&refFileElementId=refFileElement0");
        httppost.setHeader("Cookie", cookies.toString());
        MultipartEntity mpEntity = new MultipartEntity();
        ContentBody cbFile = new FileBody(file);
        mpEntity.addPart("multiupload_file", cbFile);
        httppost.setEntity(mpEntity);
        System.out.println("Now uploading your file into zoho docs...........................");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        System.out.println(response.getStatusLine());
        if (resEntity != null) {
            String tmp = EntityUtils.toString(resEntity);
            if (tmp.contains("File Uploaded Successfully")) System.out.println("File Uploaded Successfully");
        }
    }
