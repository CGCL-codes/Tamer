    private static void fileUpload() throws IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        File f = new File("e:/386241_2574873925227_1055681973_32612922_2023068366_n.jpg");
        String ext = f.getName().substring(f.getName().lastIndexOf(".") + 1);
        HttpPost httppost = null;
        if (ext.equals("jpeg") || ext.equals("jpg") || ext.equals("bmp") || ext.equals("gif") || ext.equals("png") || ext.equals("tiff")) {
            httppost = new HttpPost("http://www.imageshack.us/upload_api.php");
        }
        if (ext.equals("avi") || ext.equals("mkv") || ext.equals("mpeg") || ext.equals("mp4") || ext.equals("mov") || ext.equals("3gp") || ext.equals("flv") || ext.equals("3gpp")) {
            httppost = new HttpPost("http://render.imageshack.us/upload_api.php");
        }
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        reqEntity.addPart("Filename", new StringBody(f.getName()));
        reqEntity.addPart("optimage", new StringBody("1"));
        reqEntity.addPart("new_flash_uploader", new StringBody("y"));
        reqEntity.addPart("rembar", new StringBody("0"));
        reqEntity.addPart("myimages", new StringBody("null"));
        reqEntity.addPart("optsize", new StringBody("optimize"));
        reqEntity.addPart("rem_bar", new StringBody("0"));
        reqEntity.addPart("key", new StringBody(upload_key));
        if (login) {
            reqEntity.addPart("isUSER", new StringBody(usercookie));
            reqEntity.addPart("myimages", new StringBody(myimagescookie));
        } else {
            reqEntity.addPart("isUSER", new StringBody("null"));
        }
        reqEntity.addPart("swfupload", new StringBody("1"));
        reqEntity.addPart("ulevel", new StringBody("null"));
        reqEntity.addPart("always_opt", new StringBody("null"));
        FileBody bin = new FileBody(f);
        reqEntity.addPart("Filedata", bin);
        reqEntity.addPart("upload", new StringBody("Submit Query"));
        httppost.setEntity(reqEntity);
        System.out.println("Now uploading your file into imageshack.us Please wait......................");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
            uploadresponse = EntityUtils.toString(resEntity);
            System.out.println("PAGE :" + uploadresponse);
            uploadresponse = parseResponse(uploadresponse, "<image_link>", "<");
            System.out.println("Download Link : " + uploadresponse);
        }
    }
