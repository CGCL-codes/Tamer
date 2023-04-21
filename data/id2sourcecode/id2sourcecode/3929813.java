    public static void fileUpload() throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        file = new File("H:\\c programs.doc");
        filelength = file.length();
        if (!username.isEmpty() && !password.isEmpty()) {
            postURL = megauploadlink + "upload_done.php?UPLOAD_IDENTIFIER=" + uploadID + "&user=undefined&s=" + filelength;
        } else {
            postURL = megauploadlink + "upload_done.php?UPLOAD_IDENTIFIER=" + uploadID + "&" + usercookie + "&s=" + filelength;
        }
        HttpPost httppost = new HttpPost(postURL);
        httppost.setHeader("Cookie", usercookie);
        MultipartEntity mpEntity = new MultipartEntity();
        ContentBody cbFile = new FileBody(file);
        mpEntity.addPart("", cbFile);
        httppost.setEntity(mpEntity);
        System.out.println("Now uploading your file into megaupload...........................");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        System.out.println(response.getStatusLine());
        if (resEntity != null) {
            downloadlink = EntityUtils.toString(resEntity);
        }
    }
