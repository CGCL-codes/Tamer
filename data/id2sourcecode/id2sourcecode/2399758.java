    public static void fileUpload() throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        file = new File("H:\\Dinesh.java");
        HttpPost httppost = new HttpPost(oronlink);
        if (login) {
            httppost.setHeader("Cookie", logincookie + ";" + xfsscookie);
        }
        MultipartEntity mpEntity = new MultipartEntity();
        ContentBody cbFile = new FileBody(file);
        mpEntity.addPart("upload_type", new StringBody("file"));
        mpEntity.addPart("srv_id", new StringBody(serverID));
        if (login) {
            mpEntity.addPart("sess_id", new StringBody(xfsscookie.substring(5)));
        }
        mpEntity.addPart("srv_tmp_url", new StringBody(tmpserver));
        mpEntity.addPart("file_0", cbFile);
        httppost.setEntity(mpEntity);
        System.out.println("Now uploading your file into oron...........................");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        System.out.println(response.getStatusLine());
        if (resEntity != null) {
            String tmp = EntityUtils.toString(resEntity);
            System.out.println("Upload response : " + tmp);
            fnvalue = parseResponse(tmp, "name='fn' value='", "'");
            System.out.println("fn value : " + fnvalue);
        }
    }
