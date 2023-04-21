    public static int businessTypeUpdate(Context pContext) {
        mHttpClient = new DefaultHttpClient();
        mBuilder = new StringBuilder();
        mHttpPost = new HttpPost(new StringBuilder().append("http://").append(Enforcement.HOST).append("/ZJWHServiceTest/GIS_WHCS.asmx/DownLoadManageType").toString());
        try {
            HttpResponse lHttpReponse = mHttpClient.execute(mHttpPost);
            BufferedReader lReader = new BufferedReader(new InputStreamReader(lHttpReponse.getEntity().getContent()));
            for (String s = lReader.readLine(); null != s; s = lReader.readLine()) {
                mBuilder.append(s);
            }
            String lResponse = DataParseUtil.handleResponse(mBuilder.toString());
            List<String> businessTypeList = SystemUpdateParseUtil.businessTypeParse(lResponse);
            if (null != businessTypeList) {
                if (businessTypeList.size() > 0) {
                    BusinessTypeDBHelper lDBHelper = new BusinessTypeDBHelper(pContext);
                    lDBHelper.deleteAll();
                    for (String str : businessTypeList) {
                        ContentValues lValues = ContentValuesUtil.convertBusinessType(str);
                        lDBHelper.insert(lValues);
                    }
                    lDBHelper.closeDB();
                }
            } else {
                return 0;
            }
        } catch (Exception e) {
            return -1;
        }
        return 1;
    }
