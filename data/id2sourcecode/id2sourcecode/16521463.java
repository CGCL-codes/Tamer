    public static void fileUpload() throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(postURL);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        FileBody bin = new FileBody(new File("h:\\OpenSubtitlesHasher.java"));
        reqEntity.addPart("fff", bin);
        httppost.setEntity(reqEntity);
        System.out.println("Now uploading your file into 2shared.com. Please wait......................");
        HttpResponse response = httpclient.execute(httppost);
    }
