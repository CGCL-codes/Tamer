    private static void fileUpload() throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://www.fileden.com/upload_old.php");
        httppost.setHeader("Cookie", cookies.toString());
        file = new File("C:\\Documents and Settings\\dinesh\\Desktop\\ImageShackUploaderPlugin.java");
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        ContentBody cbFile = new FileBody(file);
        mpEntity.addPart("Filename", new StringBody(file.getName()));
        mpEntity.addPart("action", new StringBody("upload"));
        mpEntity.addPart("upload_to", new StringBody(""));
        mpEntity.addPart("overwrite_option", new StringBody("overwrite"));
        mpEntity.addPart("thumbnail_size", new StringBody("small"));
        mpEntity.addPart("create_img_tags", new StringBody("1"));
        mpEntity.addPart("file0", cbFile);
        httppost.setEntity(mpEntity);
        System.out.println("executing request " + httppost.getRequestLine());
        System.out.println("Now uploading your file into fileden");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        System.out.println(response.getStatusLine());
        if (resEntity != null) {
            uploadresponse = EntityUtils.toString(resEntity);
        }
        System.out.println(uploadresponse);
        downloadlink = parseResponse(uploadresponse, "'link':'", "'");
        System.out.println("Download link : " + downloadlink);
        httpclient.getConnectionManager().shutdown();
    }
