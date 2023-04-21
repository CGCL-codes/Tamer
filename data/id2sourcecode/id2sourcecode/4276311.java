    public static String uploadPic(PictureInfo info) throws ClientProtocolException, IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPos = new HttpPost(new StringBuilder().append("http://").append(Enforcement.HOST).append("/ZJWHServiceTest/GIS_WHCS.asmx/UploadPictureBase64").toString());
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("pictype", info.getPictype()));
        nameValuePairs.add(new BasicNameValuePair("recordid", info.getRecordid()));
        nameValuePairs.add(new BasicNameValuePair("picrecordid", info.getPicrecordid()));
        nameValuePairs.add(new BasicNameValuePair("filename", info.getFilename()));
        nameValuePairs.add(new BasicNameValuePair("fileExtension", info.getFileExtension()));
        String body = new String(com.angis.fx.util.Base64.encode(info.getFilebody()));
        nameValuePairs.add(new BasicNameValuePair("imgBase64string", body));
        httpPos.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        System.out.println(body);
        HttpResponse httpResponse = client.execute(httpPos);
        body = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        StringBuffer buffer = new StringBuffer();
        for (String str = reader.readLine(); str != null; str = reader.readLine()) {
            buffer.append(str);
        }
        System.out.println(buffer.toString());
        return buffer.toString();
    }
