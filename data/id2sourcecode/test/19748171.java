    private static void fileUpload() throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(postURL);
        httppost.setHeader("Cookie", sidcookie + ";" + ssuicookie);
        file = new File("h:/UploadingdotcomUploaderPlugin.java");
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        ContentBody cbFile = new FileBody(file);
        mpEntity.addPart("MAX_FILE_SIZE", new StringBody("314572800"));
        mpEntity.addPart("UPLOAD_IDENTIFIER", new StringBody(uploadid));
        mpEntity.addPart("DESTINATION_DIR", new StringBody(destinationdir));
        mpEntity.addPart("js_enabled", new StringBody("1"));
        mpEntity.addPart("signature", new StringBody(signature));
        mpEntity.addPart("upload_files", new StringBody(""));
        if (login) {
            mpEntity.addPart("userid", new StringBody(userid));
        }
        mpEntity.addPart("terms", new StringBody("1"));
        mpEntity.addPart("file[]", new StringBody(""));
        mpEntity.addPart("description[]", new StringBody(""));
        mpEntity.addPart("upload_file[]", cbFile);
        httppost.setEntity(mpEntity);
        System.out.println("executing request " + httppost.getRequestLine());
        System.out.println("Now uploading your file into sendspace.com");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        System.out.println(response.getStatusLine());
        if (resEntity != null) {
            uploadresponse = EntityUtils.toString(resEntity);
        }
        downloadlink = parseResponse(uploadresponse, "Download Link", "target", false);
        deletelink = parseResponse(uploadresponse, "Delete File Link", "target", false);
        downloadlink = downloadlink.replaceAll("\\s+", " ");
        deletelink = deletelink.replaceAll("\\s+", " ");
        downloadlink = parseResponse(downloadlink, "<a href=\"", "\"", false);
        deletelink = parseResponse(deletelink, "href=\"", "\"", false);
        System.out.println("Download link : " + downloadlink);
        System.out.println("Delete link : " + deletelink);
        httpclient.getConnectionManager().shutdown();
    }
