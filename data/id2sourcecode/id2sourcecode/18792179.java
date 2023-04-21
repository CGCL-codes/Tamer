    public static int gisZFJGUpdate(Context pContext) {
        mHttpClient = new DefaultHttpClient();
        mBuilder = new StringBuilder();
        mHttpPost = new HttpPost(new StringBuilder().append("http://").append(Enforcement.HOST).append("/ZJWHServiceTest/GIS_Duty.asmx/DownLoadZFJG").toString());
        try {
            HttpResponse lHttpReponse = mHttpClient.execute(mHttpPost);
            BufferedReader lReader = new BufferedReader(new InputStreamReader(lHttpReponse.getEntity().getContent()));
            for (String s = lReader.readLine(); null != s; s = lReader.readLine()) {
                mBuilder.append(s);
            }
            String lResponse = DataParseUtil.handleResponse(mBuilder.toString());
            List<GISZFJGInfo> gisZFJGList = SystemUpdateParseUtil.gisZFJGParse(lResponse);
            if (null != gisZFJGList) {
                if (gisZFJGList.size() > 0) {
                    GISZFJGDBHelper lDBHelper = new GISZFJGDBHelper(pContext);
                    lDBHelper.deleteAll();
                    for (GISZFJGInfo gisZFJGInfo : gisZFJGList) {
                        ContentValues lValues = ContentValuesUtil.convertGISZFJG(gisZFJGInfo);
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
