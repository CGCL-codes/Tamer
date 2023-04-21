    public static int uploadLoginInfo(Context pContext, ContextInfo contextInfo) {
        mHttpClient = new DefaultHttpClient();
        mBuilder = new StringBuilder();
        mHttpPost = new HttpPost(new StringBuilder().append("http://").append(Enforcement.HOST).append("/ZJWHServiceTest/GIS_Duty.asmx/PDALoginRecord").toString());
        try {
            List<NameValuePair> lNameValuePairs = new ArrayList<NameValuePair>(2);
            StringBuilder lBuilder = new StringBuilder();
            lBuilder.append(LoginActivity.mLoginId).append("&").append(LoginActivity.g_username).append("&&&").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(contextInfo.getLoginTime())).append("&").append(contextInfo.getDeviceno()).append("&&").append(pContext.getString(R.string.appversion)).append("#");
            lNameValuePairs.add(new BasicNameValuePair("loginInfos", lBuilder.toString()));
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
