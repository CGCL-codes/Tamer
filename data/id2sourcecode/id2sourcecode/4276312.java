    public static String uploadCsCoord(ChangsuoInformation info) throws ClientProtocolException, IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPos = new HttpPost(new StringBuilder().append("http://").append(Enforcement.HOST).append("/ZJWHServiceTest/GIS_WHCS/RecieveWHCSXY").toString());
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        StringBuilder lBuilder = new StringBuilder();
        lBuilder.append(info.getCsId()).append("&").append(info.getXlonGPS()).append("&").append(info.getYlatGPS()).append("#");
        nameValuePairs.add(new BasicNameValuePair("coorInfo", lBuilder.toString()));
        httpPos.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse httpResponse = client.execute(httpPos);
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        StringBuffer buffer = new StringBuffer();
        for (String str = reader.readLine(); str != null; str = reader.readLine()) {
            buffer.append(str);
        }
        return buffer.toString();
    }
