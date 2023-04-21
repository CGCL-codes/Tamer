    public static int uploadDutyInfo(Context pContext, DutyInfo pDutyInfo) {
        mHttpClient = new DefaultHttpClient();
        mBuilder = new StringBuilder();
        mHttpPost = new HttpPost(new StringBuilder().append("http://").append(Enforcement.HOST).append("/ZJWHServiceTest/GIS_Duty.asmx/DutyStationInfo").toString());
        try {
            List<NameValuePair> lNameValuePairs = new ArrayList<NameValuePair>(2);
            StringBuilder lBuilder = new StringBuilder();
            lBuilder.append(pDutyInfo.getDutyno()).append("~").append(pDutyInfo.getDusername()).append("~").append(SxryMActivity.SXRY_USER_DEPARTMENT).append("~").append(SxryActivity.QTRY_EDIT_MEMBER).append("~").append(pDutyInfo.getStartdutytime()).append("~").append(pDutyInfo.getEnddutytime()).append("~").append(Enforcement.mDeviceId).append("~").append(LoginActivity.mLoginId).append("^");
            lNameValuePairs.add(new BasicNameValuePair("stationinfo", lBuilder.toString()));
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
