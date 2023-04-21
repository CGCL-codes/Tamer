    private static void fileUpload() throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(postURL);
        httppost.setHeader("Cookie", sidcookie + ";" + mysessioncookie);
        generateZShareID();
        file = new File("g:/Way2SMSClient.7z");
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        ContentBody cbFile = new FileBody(file);
        mpEntity.addPart("", cbFile);
        mpEntity.addPart("TOS", new StringBody("1"));
        httppost.setEntity(mpEntity);
        System.out.println("executing request " + httppost.getRequestLine());
        System.out.println("Now uploading your file into zshare.net");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        System.out.println(response.getStatusLine());
        if (resEntity != null) {
            uploadresponse = EntityUtils.toString(resEntity);
        }
        uploadresponse = uploadresponse.replaceAll("\n", "");
        uploadresponse = uploadresponse.substring(uploadresponse.indexOf("index2.php"));
        uploadresponse = uploadresponse.substring(0, uploadresponse.indexOf("\">here"));
        uploadresponse = uploadresponse.replaceAll("amp;", "");
        uploadresponse = zsharelink + uploadresponse;
        uploadresponse = uploadresponse.replaceAll(" ", "%20");
        System.out.println("shew : " + uploadresponse);
        httpclient.getConnectionManager().shutdown();
    }
