    private static void fileUpload() throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        File f = new File("E:/Projects/NU/NeembuuUploader/test/neembuuuploader/test/plugins/BayFilesUploaderPlugin.java");
        HttpPost httppost = new HttpPost("http://api.scribd.com/api?method=docs.upload&api_key=5t8cd1k0ww3iupw31bb2a&Session_key=" + session_key);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        FileBody bin = new FileBody(f);
        reqEntity.addPart("file", bin);
        httppost.setEntity(reqEntity);
        System.out.println("Now uploading your file into scribd........ Please wait......................");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
            doc_id = EntityUtils.toString(resEntity);
            System.out.println(doc_id);
            if (doc_id.contains("stat=\"ok\"")) {
                doc_id = parseResponse(doc_id, "<doc_id>", "</doc_id>");
                System.out.println("doc id :" + doc_id);
            } else {
                throw new Exception("There might be problem with your internet connection or server error. Please try again some after time :(");
            }
        } else {
            throw new Exception("There might be problem with your internet connection or server error. Please try again some after time :(");
        }
    }
