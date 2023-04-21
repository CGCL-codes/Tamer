    private static void fileUpload() throws Exception {
        file = new File("C:\\Documents and Settings\\dinesh\\Desktop\\NU.txt");
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(postURL);
        if (login) {
            httppost.setHeader("Cookie", usercookie);
        }
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        ContentBody cbFile = new FileBody(file);
        if (login) {
            mpEntity.addPart("upload_hash", new StringBody(upload_hash));
        }
        mpEntity.addPart("file", cbFile);
        httppost.setEntity(mpEntity);
        System.out.println("executing request " + httppost.getRequestLine());
        System.out.println("Now uploading your file into netload");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        System.out.println(response.getStatusLine());
        Header firstHeader = response.getFirstHeader("Location");
        System.out.println(firstHeader.getValue());
        uploadresponse = getData(firstHeader.getValue());
        System.out.println("Upload response : " + uploadresponse);
        downloadlink = parseResponse(uploadresponse, "The download link is: <br/>", "\" target=\"_blank\">");
        downloadlink = downloadlink.substring(downloadlink.indexOf("href=\""));
        downloadlink = downloadlink.replace("href=\"", "");
        System.out.println("download link : " + downloadlink);
        deletelink = parseResponse(uploadresponse, "The deletion link is: <br/>", "\" target=\"_blank\">");
        deletelink = deletelink.substring(deletelink.indexOf("href=\""));
        deletelink = deletelink.replace("href=\"", "");
        System.out.println("delete link : " + deletelink);
        httpclient.getConnectionManager().shutdown();
    }
