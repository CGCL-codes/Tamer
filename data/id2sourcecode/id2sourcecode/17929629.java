    private static void fileUpload() throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://dl-web.dropbox.com/upload");
        httppost.setHeader("Referer", "https://www.dropbox.com/home/Public");
        httppost.setHeader("Cookie", forumjarcookie + ";" + forumlidcookie + ";" + touchcookie);
        file = new File("C:\\Documents and Settings\\dinesh\\Desktop\\GigaSizeUploaderPlugin.java");
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        ContentBody cbFile = new FileBody(file);
        mpEntity.addPart("t", new StringBody(tvalue));
        mpEntity.addPart("plain", new StringBody("yes"));
        mpEntity.addPart("dest", new StringBody("/Public"));
        mpEntity.addPart("file", cbFile);
        httppost.setEntity(mpEntity);
        System.out.println("executing request " + httppost.getRequestLine());
        System.out.println("Now uploading your file into dropbox.com");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        System.out.println(response.getStatusLine());
        if (resEntity != null) {
            uploadresponse = EntityUtils.toString(resEntity);
        }
        System.out.println("Upload response : " + uploadresponse);
        if (uploadresponse.contains("The resource was found at https://www.dropbox.com/home/Public")) {
            System.out.println("Downloadlink : http://dl.dropbox.com/u/" + uid + "/" + (URLEncoder.encode(file.getName(), "UTF-8").replace("+", "%20")));
        } else {
            System.out.println("Upload failed");
        }
    }
