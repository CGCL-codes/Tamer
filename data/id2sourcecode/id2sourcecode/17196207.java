    private static void fileUpload() throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(postURL);
        httppost.setHeader("Cookie", sidcookie);
        file = new File("h:/install.txt");
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        ContentBody cbFile = new FileBody(file);
        mpEntity.addPart("filepc", cbFile);
        mpEntity.addPart("server", new StringBody(server));
        httppost.setEntity(mpEntity);
        System.out.println("executing request " + httppost.getRequestLine());
        System.out.println("Now uploading your file into uploadbox.com");
        HttpResponse response = httpclient.execute(httppost);
        System.out.println(response.getStatusLine());
        uploadresponse = response.getLastHeader("Location").getValue();
        uploadresponse = getData(uploadresponse);
        downloadlink = parseResponse(uploadresponse, "name=\"loadlink\" id=\"loadlink\" class=\"text\" onclick=\"this.select();\" value=\"", "\"");
        deletelink = parseResponse(uploadresponse, "name=\"deletelink\" id=\"deletelink\" class=\"text\" onclick=\"this.select();\" value=\"", "\"");
        System.out.println("Download link " + downloadlink);
        System.out.println("deletelink : " + deletelink);
    }
