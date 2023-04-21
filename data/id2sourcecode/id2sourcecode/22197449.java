    private static void fileUpload() throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(postURL);
        file = new File("C:\\Documents and Settings\\dinesh\\Desktop\\MegaUploadUploaderPlugin.java");
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        ContentBody cbFile = new FileBody(file);
        mpEntity.addPart("Filename", new StringBody(file.getName()));
        if (login) {
            System.out.println("adding php sess .............");
            mpEntity.addPart("PHPSESSID", new StringBody(sessionid));
        }
        mpEntity.addPart("Filedata", cbFile);
        httppost.setEntity(mpEntity);
        System.out.println("executing request " + httppost.getRequestLine());
        System.out.println("Now uploading your file into crocko");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        System.out.println(response.getStatusLine());
        if (resEntity != null) {
            uploadresponse = EntityUtils.toString(resEntity);
        }
        System.out.println("Upload response : " + uploadresponse);
        downloadlink = parseResponse(uploadresponse, "Download link:", "</dd>");
        downloadlink = parseResponse(downloadlink, "value=\"", "\"");
        deletelink = parseResponse(uploadresponse, "Delete link:", "</a></dd>");
        deletelink = deletelink.substring(deletelink.indexOf("http://"));
        System.out.println("Download link : " + downloadlink);
        System.out.println("Delete link : " + deletelink);
    }
