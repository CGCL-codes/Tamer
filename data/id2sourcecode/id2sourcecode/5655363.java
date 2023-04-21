    public static void fileUpload() throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        file = new File("C:\\Documents and Settings\\dinesh\\Desktop\\FourSharedUploaderPlugin.java");
        HttpPost httppost = new HttpPost("http://upload.1fichier.com/en/upload.cgi?id=" + uid);
        if (login) {
            httppost.setHeader("Cookie", sidcookie);
        }
        MultipartEntity mpEntity = new MultipartEntity();
        ContentBody cbFile = new FileBody(file);
        mpEntity.addPart("file[]", cbFile);
        mpEntity.addPart("domain", new StringBody("0"));
        httppost.setEntity(mpEntity);
        System.out.println("Now uploading your file into 1fichier...........................");
        System.out.println("Now executing......." + httppost.getRequestLine());
        HttpResponse response = httpclient.execute(httppost);
        System.out.println(response.getStatusLine());
        if (response.containsHeader("Location")) {
            uploadresponse = response.getFirstHeader("Location").getValue();
            System.out.println("Upload location link : " + uploadresponse);
        } else {
            throw new Exception("There might be a problem with your internet connection or server error. Please try again");
        }
    }
