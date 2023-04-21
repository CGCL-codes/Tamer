    private static void fileUpload() throws Exception {
        file = new File("C:\\Documents and Settings\\All Users\\Documents\\My Pictures\\Sample Pictures\\Sunset.jpg");
        httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(downURL + "cgi-bin/upload.cgi?sid=" + sid);
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        ContentBody cbFile = new FileBody(file);
        mpEntity.addPart("file[0]", cbFile);
        httppost.setEntity(mpEntity);
        System.out.println("executing request " + httppost.getRequestLine());
        System.out.println("Now uploading your file into divshare.com");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        System.out.println(response.getStatusLine());
        System.out.println(EntityUtils.toString(resEntity));
    }
