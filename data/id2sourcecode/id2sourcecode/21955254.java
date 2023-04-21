    private static void fileUpload() throws Exception {
        file = new File("C:\\Documents and Settings\\dinesh\\Desktop\\ImageShackUploaderPlugin.java");
        httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(postURL);
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        ContentBody cbFile = new FileBody(file);
        mpEntity.addPart("Filename", new StringBody(file.getName()));
        mpEntity.addPart("ssd", new StringBody(ssd));
        mpEntity.addPart("Filedata", cbFile);
        httppost.setEntity(mpEntity);
        System.out.println("executing request " + httppost.getRequestLine());
        System.out.println("Now uploading your file into slingfile.com");
        HttpResponse response = httpclient.execute(httppost);
        System.out.println(response.getStatusLine());
        System.out.println(EntityUtils.toString(response.getEntity()));
    }
