    private static void fileUpload() throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(postURL);
        file = new File("C:\\Documents and Settings\\dinesh\\Desktop\\FileSonicUploaderPlugin.java");
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        ContentBody cbFile = new FileBody(file);
        mpEntity.addPart("akey", new StringBody(apikey));
        mpEntity.addPart("Filedata", cbFile);
        httppost.setEntity(mpEntity);
        System.out.println("executing request " + httppost.getRequestLine());
        System.out.println("Now uploading your file into ifile.it");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        httpclient.getConnectionManager().shutdown();
        System.out.println(response.getStatusLine());
        if (resEntity != null) {
            uploadresponse = EntityUtils.toString(resEntity);
            System.out.println("Upload response : " + uploadresponse);
        } else {
            throw new Exception("There might be a problem with your internet connection or server error. Please try after some time :(");
        }
        if (uploadresponse.contains("\"status\":\"ok\"")) {
            System.out.println("File uploaded successfully :)");
            file_ukey = parseResponse(uploadresponse, "\"ukey\":\"", "\"");
            System.out.println("File ukey : " + file_ukey);
            downloadlink = "http://ifile.it/" + file_ukey + "/" + file.getName();
            System.out.println("Download link : " + downloadlink);
        } else {
            throw new Exception("There might be a problem with your internet connection or server error. Please try after some time :(");
        }
    }
