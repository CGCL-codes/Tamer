    public static void fileUpload() throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://upload.filefactory.com/upload.php");
        httppost.setHeader("Cookie", cookie + ";" + membershipcookie);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        FileBody bin = new FileBody(new File("h:\\Learning Plan 1.0.pdf"));
        reqEntity.addPart("Filedata", bin);
        reqEntity.addPart("upload", new StringBody("Submit Query"));
        httppost.setEntity(reqEntity);
        System.out.println("Now uploading your file into filefactory.com. Please wait......................");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        String page = "";
        if (resEntity != null) {
            page = EntityUtils.toString(resEntity);
            System.out.println("PAGE :" + page);
        }
        downloadlink = getData("http://www.filefactory.com/mupc/" + page);
        downloadlink = downloadlink.substring(downloadlink.indexOf("<div class=\"metadata\">"));
        downloadlink = downloadlink.replace("<div class=\"metadata\">", "");
        downloadlink = downloadlink.substring(0, downloadlink.indexOf("<"));
        downloadlink = downloadlink.trim();
        System.out.println("Download Link : " + downloadlink);
    }
