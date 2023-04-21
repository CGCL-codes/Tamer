    public static int ecnomicTypeUpdate(Context pContext) {
        mHttpClient = new DefaultHttpClient();
        mBuilder = new StringBuilder();
        mHttpPost = new HttpPost(new StringBuilder().append("http://").append(Enforcement.HOST).append("/ZJWHServiceTest/GIS_WHCS.asmx/DownLoadEconomyType").toString());
        try {
            HttpResponse lHttpResponse = mHttpClient.execute(mHttpPost);
            BufferedReader lReader = new BufferedReader(new InputStreamReader(lHttpResponse.getEntity().getContent()));
            for (String s = lReader.readLine(); s != null; s = lReader.readLine()) {
                mBuilder.append(s);
            }
            String lResponse = DataParseUtil.handleResponse(mBuilder.toString());
            List<String> lEcnomicTypeList = SystemUpdateParseUtil.ecnomicTypeParse(lResponse);
            if (null != lEcnomicTypeList) {
                if (lEcnomicTypeList.size() > 0) {
                    EcnomicTypeDBHelper lDBHelper = new EcnomicTypeDBHelper(pContext);
                    lDBHelper.deleteAll();
                    for (String str : lEcnomicTypeList) {
                        ContentValues lValues = ContentValuesUtil.convertEcnomicType(str);
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
