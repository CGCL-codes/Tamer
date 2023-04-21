    public static int newChangsuo(Context pContext, String pCsInfo) {
        mHttpClient = new DefaultHttpClient();
        mBuilder = new StringBuilder();
        mHttpPost = new HttpPost(new StringBuilder().append("http://").append(Enforcement.HOST).append("/ZJWHServiceTest/GIS_WHCS.asmx/AddNewWHCS").toString());
        try {
            List<NameValuePair> lNameValuePairs = new ArrayList<NameValuePair>(2);
            lNameValuePairs.add(new BasicNameValuePair("csInfo", pCsInfo));
            mHttpPost.setEntity(new UrlEncodedFormEntity(lNameValuePairs));
            HttpResponse lHttpReponse = mHttpClient.execute(mHttpPost);
            BufferedReader lReader = new BufferedReader(new InputStreamReader(lHttpReponse.getEntity().getContent()));
            for (String s = lReader.readLine(); null != s; s = lReader.readLine()) {
                mBuilder.append(s);
            }
            String lResponse = DataParseUtil.handleResponse(mBuilder.toString());
            if (!lResponse.equals("1")) {
                return 0;
            }
        } catch (Exception e) {
            return -1;
        }
        return 1;
    }
