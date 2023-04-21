    public static int ckTypeUpdate(Context pContext) {
        mHttpClient = new DefaultHttpClient();
        mBuilder = new StringBuilder();
        mHttpPost = new HttpPost(new StringBuilder().append("http://").append(Enforcement.HOST).append("/ZJWHServiceTest/GIS_CheckDaily.asmx/DownLoadJCJGInfo").toString());
        try {
            HttpResponse lHttpResponse = mHttpClient.execute(mHttpPost);
            BufferedReader lReader = new BufferedReader(new InputStreamReader(lHttpResponse.getEntity().getContent()));
            for (String s = lReader.readLine(); s != null; s = lReader.readLine()) {
                mBuilder.append(s);
            }
            String lResponse = DataParseUtil.handleResponse(mBuilder.toString());
            Map<String, List<CKTypeInfo>> lCKTypeInfoMap = SystemUpdateParseUtil.CKResultParse(lResponse);
            if (lCKTypeInfoMap.get("WGTYPE").size() > 0 || lCKTypeInfoMap.get("ZCTYPE").size() > 0) {
                WGTypeDBHelper lWGTypeDBHelper = new WGTypeDBHelper(pContext);
                lWGTypeDBHelper.delAll();
                List<CKTypeInfo> lWGTypeInfoList = lCKTypeInfoMap.get("WGTYPE");
                ContentValues lValues = null;
                for (CKTypeInfo wgTypeInfo : lWGTypeInfoList) {
                    lValues = ContentValuesUtil.convertWGType(wgTypeInfo);
                    lWGTypeDBHelper.insertWGType(lValues);
                }
                lWGTypeDBHelper.closeDB();
                ZCTypeDBHelper lZCTypeDBHelper = new ZCTypeDBHelper(pContext);
                lZCTypeDBHelper.delAll();
                List<CKTypeInfo> lZCTypeInfoList = lCKTypeInfoMap.get("ZCTYPE");
                for (CKTypeInfo zcTypeInfo : lZCTypeInfoList) {
                    lValues = ContentValuesUtil.convertZCType(zcTypeInfo);
                    lZCTypeDBHelper.insertWGType(lValues);
                }
                lZCTypeDBHelper.closeDB();
            } else {
                return 0;
            }
        } catch (Exception e) {
            return -1;
        }
        return 1;
    }
