    public static void fileUpload() throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        file = new File("C:\\Documents and Settings\\All Users\\Documents\\My Pictures\\Sample Pictures\\Winter.jpg");
        HttpPost httppost = new HttpPost("http://www.gigasize.com/uploadie");
        httppost.setHeader("Cookie", cookies.toString());
        MultipartEntity mpEntity = new MultipartEntity();
        ContentBody cbFile = new FileBody(file);
        mpEntity.addPart("UPLOAD_IDENTIFIER", new StringBody(uploadid));
        mpEntity.addPart("sid", new StringBody(sid));
        mpEntity.addPart("fileUpload1", cbFile);
        httppost.setEntity(mpEntity);
        System.out.println("Now uploading your file into Gigasize...........................");
        System.out.println("Now executing......." + httppost.getRequestLine());
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        System.out.println(response.getStatusLine());
        if (resEntity != null) {
            sid = "";
            sid = EntityUtils.toString(resEntity);
            System.out.println("After upload sid value : " + sid);
        }
    }
